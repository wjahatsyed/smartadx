package com.smartadx.adservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final AdCampaignRepository adCampaignRepository;

    private static final String CACHE_KEY = "active_campaigns";

    public void cacheActiveCampaigns(List<AdCampaign> campaigns) {
        redisTemplate.opsForValue().set(CACHE_KEY, campaigns);
    }

    public List<AdCampaign> getActiveCampaigns() {
        Object cached = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cached != null) {
            return objectMapper.convertValue(cached, new TypeReference<>() {});
        }

        // 🟡 Cache miss — fallback to DB and re-cache
        List<AdCampaign> allCampaigns = adCampaignRepository.findAll();
        refreshCache(allCampaigns); // Refill Redis with filtered active campaigns

        return getActiveCampaigns(); // Try again now that it's cached
    }

    public void refreshCache(List<AdCampaign> allCampaigns) {
        List<AdCampaign> active = allCampaigns.stream()
                .filter(c -> c.getStartDate().isBefore(LocalDateTime.now()) &&
                        c.getEndDate().isAfter(LocalDateTime.now()) &&
                        c.getBudget() > 0)
                .collect(Collectors.toList());

        cacheActiveCampaigns(active);
    }

    // ✅ New method for app startup or API refresh
    public void populateActiveCampaignCache() {
        List<AdCampaign> allCampaigns = adCampaignRepository.findAll();
        refreshCache(allCampaigns);
    }
}
