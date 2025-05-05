package com.smartadx.adservice.service;

import com.smartadx.adservice.dto.TargetedAdResponse;
import com.smartadx.adservice.dto.UserProfileDto;
import com.smartadx.adservice.model.AdCampaign;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TargetingService {

    private final CampaignCacheService cacheService;

    @PostConstruct
    public void init() {
        cacheActiveCampaigns(); // Populate Redis cache at startup
    }

    public void cacheActiveCampaigns() {
        cacheService.populateActiveCampaignCache(); // Delegated to cache layer
    }

    public TargetedAdResponse getBestMatchingAd(UserProfileDto user) {
        List<AdCampaign> campaigns = cacheService.getActiveCampaigns();

        return campaigns.stream()
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
