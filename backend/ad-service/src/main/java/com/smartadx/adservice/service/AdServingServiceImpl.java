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

        return repository.findAll().stream()
                .filter(c -> !c.getStartDate().isAfter(now) && !c.getEndDate().isBefore(now)) // active date
                .filter(c -> c.getImpressions() > 0 && c.getBudget() > 0) // has budget & impressions
                .filter(c -> {
                    Set<String> campaignKeywords = Arrays.stream(c.getTargetingKeywords().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());
                    return campaignKeywords.stream().anyMatch(userKeywords::contains);
                })
                .sorted(Comparator.comparingDouble(AdCampaign::getBudget).reversed()) // prioritize by budget
                .findFirst();
    }
}
