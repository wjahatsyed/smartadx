package com.smartadx.adservice.service;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdServingServiceTest {

    @Mock
    private AdCampaignRepository repository;

    @InjectMocks
    private AdServingServiceImpl adServingService;

    private AdCampaign matchingCampaign;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        matchingCampaign = AdCampaign.builder()
                .id(1L)
                .advertiser("Adidas")
                .content("https://ads.com/banner-adidas.jpg")
                .interestTags(List.of("sports", "shoes", "fitness"))
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(5))
                .budget(5000.0)
                .impressions(100)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();
    }

    @Test
    void shouldServeMatchingAdCampaign() {
        when(repository.findAll()).thenReturn(List.of(matchingCampaign));

        Optional<AdCampaign> result = adServingService.serveAd(List.of("shoes", "health"));

        assertThat(result).isPresent();
        assertThat(result.get().getAdvertiser()).isEqualTo("Adidas");
    }

    @Test
    void shouldReturnEmptyWhenNoMatch() {
        when(repository.findAll()).thenReturn(List.of(matchingCampaign));

        Optional<AdCampaign> result = adServingService.serveAd(List.of("finance", "technology"));

        assertThat(result).isEmpty();
    }
}
