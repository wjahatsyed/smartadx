package com.smartadx.adservice.service;

import com.smartadx.adservice.dto.*;
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

    @PostConstruct
    public void init() {
        cacheActiveCampaigns(); // Populate Redis cache at startup
    }

    public void cacheActiveCampaigns() {
        cacheService.populateActiveCampaignCache(); // Pull from DB, convert to DTOs, and cache
    }

    public TargetedAdResponse getBestMatchingAd(UserProfileDto user) {
        List<AdCampaignDTO> campaigns = cacheService.getActiveCampaigns();

        TargetedAdResponse bestMatch = campaigns.stream()
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

        if (bestMatch != null) {
            adEventProducer.sendAdEvent(
                    AdEventDto.builder()
                            .campaignId(bestMatch.getCampaignId())
                            .type("IMPRESSION")
                            .userId("user123") // dynamic if available
                            .location(user.getLocation())
                            .ip(user.getIp())
                            .timestamp(LocalDateTime.now())
                            .build()
            );
        }
        return bestMatch;
    }

    private double calculateScore(AdCampaignDTO campaign, UserProfileDto user) {
        double score = 0.0;

        if (campaign.getInterestTags() == null) return 0.0;

        Set<String> keywords = Set.of(
                (campaign.getLocation() + "," +
                        campaign.getAgeGroup() + "," +
                        String.join(",", campaign.getInterestTags())).toLowerCase().split(",")
        );

        if (keywords.contains(user.getLocation().toLowerCase())) score += 0.4;
        if (keywords.contains(String.valueOf(user.getAge()))) score += 0.2;

        long matchedInterests = user.getInterests().stream()
                .filter(i -> keywords.contains(i.toLowerCase()))
                .count();

        score += matchedInterests * 0.1;

        return score;
    }

    public void trackClick(ClickRequest request) {
        adEventProducer.sendAdEvent(
                AdEventDto.builder()
                        .campaignId(request.getCampaignId())
                        .type("CLICK")
                        .userId(request.getUserId())
                        .location(request.getLocation())
                        .ip(request.getIp())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
