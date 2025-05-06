package com.smartadx.adservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.dto.AdCampaignDTO;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AdCampaignRepository adCampaignRepository;

    private static final String CACHE_KEY = "active_campaigns";

    public void cacheActiveCampaigns(List<AdCampaignDTO> campaigns) {
        redisTemplate.opsForValue().set(CACHE_KEY, campaigns);
    }

    public List<AdCampaignDTO> getActiveCampaigns() {
        Object cached = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cached != null) {
            return objectMapper.convertValue(cached, new TypeReference<>() {
            });
        }

        // 🟡 Cache miss — fallback to DB and re-cache
        List<AdCampaign> allCampaigns = adCampaignRepository.findAllWithInterests();
        List<AdCampaignDTO> adCampaignDTOS = toDTOList(allCampaigns);
        refreshCache(adCampaignDTOS); // Refill Redis with filtered active campaigns

        return getActiveCampaigns();
    }

    public void refreshCache(List<AdCampaignDTO> allCampaigns) {
        List<AdCampaignDTO> active = allCampaigns.stream()
                .filter(c -> c.getStartDate().isBefore(LocalDateTime.now()) &&
                        c.getEndDate().isAfter(LocalDateTime.now()) &&
                        c.getBudget() > 0)
                .collect(Collectors.toList());

        cacheActiveCampaigns(active);
    }

    // ✅ New method for app startup or API refresh
    public void populateActiveCampaignCache() {
        List<AdCampaign> allCampaigns = adCampaignRepository.findAllWithInterests();
        List<AdCampaignDTO> adCampaignDTOS = toDTOList(allCampaigns);
        refreshCache(adCampaignDTOS);
    }

    private static List<AdCampaignDTO> toDTOList(List<AdCampaign> campaignList) {
        List<AdCampaignDTO> adCampaignDTOList = new ArrayList<>();
        for (AdCampaign campaign : campaignList) {
            AdCampaignDTO adCampaignDTO = AdCampaignDTO.builder()
                    .id(campaign.getId())
                    .advertiser(campaign.getAdvertiser())
                    .content(campaign.getContent())
                    .type(campaign.getType())
                    .location(campaign.getLocation())
                    .ageGroup(campaign.getAgeGroup())
                    .interestTags(campaign.getInterestTags())
                    .budget(campaign.getBudget())
                    .remainingBudget(campaign.getRemainingBudget())
                    .impressions(campaign.getImpressions())
                    .clicks(campaign.getClicks())
                    .startDate(campaign.getStartDate())
                    .endDate(campaign.getEndDate())
                    .createdAt(campaign.getCreatedAt())
                    .build();
            adCampaignDTOList.add(adCampaignDTO);
        }
        return adCampaignDTOList;
    }


}
