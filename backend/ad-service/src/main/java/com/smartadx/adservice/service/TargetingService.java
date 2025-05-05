package com.smartadx.adservice.service;

import com.smartadx.adservice.dto.AdEventDto;
import com.smartadx.adservice.dto.TargetedAdResponse;
import com.smartadx.adservice.dto.UserProfileDto;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.producer.AdEventProducer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TargetingService {

    private final CampaignCacheService cacheService;
    private final AdEventProducer adEventProducer;
    private final AdEventDto adEventDto;

    @PostConstruct
    public void init() {
        cacheActiveCampaigns(); // Populate Redis cache at startup
    }

    public void cacheActiveCampaigns() {
        cacheService.populateActiveCampaignCache(); // Delegated to cache layer
    }

    public TargetedAdResponse getBestMatchingAd(UserProfileDto user) {
        List<AdCampaign> campaigns = cacheService.getActiveCampaigns();
        TargetedAdResponse targetedAdResponse =
        campaigns.stream()
                .map(campaign -> {
                    double score = calculateScore(campaign, user);
                    return TargetedAdResponse.builder()
                            .campaignId(campaign.getId())
                            .content(campaign.getContent())
                            .score(score)
                            .build();
                })
                .filter(ad -> ad.getScore() > 0)
                .max(Comparator.comparing(TargetedAdResponse::getScore))
                .orElse(null);
        if(targetedAdResponse != null){
            adEventProducer.sendAdEvent(
                    AdEventDto.builder()
                            .campaignId(targetedAdResponse.getCampaignId())
                            .type("IMPRESSION")
                            .userId("user123") // or from request
                            .location(user.getLocation())
                            .ip(user.getIp())
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }

        return targetedAdResponse;
    }

    private double calculateScore(AdCampaign campaign, UserProfileDto user) {
        double score = 0.0;

        if (campaign.getTargetingKeywords() == null) return 0.0;

        Set<String> keywords = Set.of(campaign.getTargetingKeywords().toLowerCase().split(","));

        if (keywords.contains(user.getLocation().toLowerCase())) score += 0.4;
        if (keywords.contains(String.valueOf(user.getAge()))) score += 0.2;

        long matchedInterests = user.getInterests().stream()
                .filter(i -> keywords.contains(i.toLowerCase()))
                .count();

        score += matchedInterests * 0.1;

        return score;
    }
}
