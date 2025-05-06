package com.smartadx.adservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.dto.AdCampaignDTO;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class CampaignCacheServiceTest {

    @Mock
    private AdCampaignRepository adCampaignRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOps;

    @InjectMocks
    private CampaignCacheService campaignCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        campaignCacheService = new CampaignCacheService(redisTemplate, new ObjectMapper(), adCampaignRepository);
    }

   /* @Test
    void populateActiveCampaignCache_shouldCacheOnlyActiveCampaigns() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        AdCampaign active = AdCampaign.builder()
                .id(1L)
                .content("Buy Shoes!")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .budget(100.00)
                .build();

        AdCampaign expired = AdCampaign.builder()
                .id(2L)
                .content("Old Campaign")
                .startDate(now.minusDays(10))
                .endDate(now.minusDays(1))
                .budget(100.00)
                .build();

        when(adCampaignRepository.findAll()).thenReturn(List.of(active, expired));

        // Act
        campaignCacheService.populateActiveCampaignCache();

        // Assert: Verify only the active campaign is cached as AdCampaignDTO
        verify(valueOps, times(1)).set(eq("active_campaigns"), argThat(campaigns -> {
            @SuppressWarnings("unchecked")
            List<AdCampaignDTO> list = (List<AdCampaignDTO>) campaigns;
            return list.size() == 1 && list.getFirst().getId().equals(1L);
        }));
    }*/
}
