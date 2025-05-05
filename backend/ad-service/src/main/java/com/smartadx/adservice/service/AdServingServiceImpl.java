package com.smartadx.adservice.service;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdServingServiceImpl implements AdServingService {

    private final AdCampaignRepository repository;

    public AdServingServiceImpl(AdCampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AdCampaign> serveAd(List<String> userKeywords) {
        LocalDateTime now = LocalDateTime.now();

        // active date
        // has budget & impressions
        // prioritize by budget
        List<AdCampaign> toSort = new ArrayList<>();
        for (AdCampaign adCampaign : repository.findAll()) {
            if (!adCampaign.getStartDate().isAfter(now) && !adCampaign.getEndDate().isBefore(now)) {
                if (adCampaign.getImpressions() > 0 && adCampaign.getBudget() > 0) {
                    Set<String> campaignKeywords = adCampaign.getInterestTags().stream()
                            .map(String::trim)
                            .collect(Collectors.toSet());
                    if (campaignKeywords.stream().anyMatch(userKeywords::contains)) {
                        toSort.add(adCampaign);
                    }
                }
            }
        }
        toSort.sort(Comparator.comparingDouble(AdCampaign::getBudget).reversed());
        for (AdCampaign adCampaign : toSort) {
            return Optional.of(adCampaign);
        }
        return Optional.empty();
    }
}
