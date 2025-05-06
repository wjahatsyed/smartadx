package com.smartadx.adservice.service;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdCampaignServiceTest {

    @Mock
    private AdCampaignRepository repository;

    @InjectMocks
    private AdCampaignService service;

    private AdCampaign sampleCampaign;

    @BeforeEach
    void setUp() {
        sampleCampaign = AdCampaign.builder()
                .id(1L)
                .advertiser("Nike")
                .content("https://ads.com/banner1.jpg")
                .interestTags(List.of("sports", "shoes"))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .budget(5000.0)
                .impressions(1000)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnAllCampaigns() {
        when(repository.findAll()).thenReturn(List.of(sampleCampaign));

        List<AdCampaign> result = service.getAllCampaigns();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAdvertiser()).isEqualTo("Nike");
    }

    @Test
    void shouldReturnCampaignById() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleCampaign));

        AdCampaign result = service.getCampaignById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).contains("banner1.jpg");
    }

    @Test
    void shouldCreateCampaign() {
        when(repository.save(any(AdCampaign.class))).thenReturn(sampleCampaign);

        AdCampaign result = service.createCampaign(sampleCampaign);

        assertThat(result.getAdvertiser()).isEqualTo("Nike");
    }

    @Test
    void shouldUpdateCampaign() {
        AdCampaign updated = sampleCampaign.toBuilder()
                .content("https://ads.com/banner2.jpg")
                .budget(6000.0)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(sampleCampaign));
        when(repository.save(any())).thenReturn(updated);

        AdCampaign result = service.updateCampaign(1L, updated);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).contains("banner2.jpg");
        assertThat(result.getBudget()).isEqualTo(6000.0);
    }

    @Test
    void shouldDeleteCampaign() throws Exception {
        // Mock that campaign exists
        when(repository.existsById(1L)).thenReturn(true);

        // Mock the delete method to do nothing
        doNothing().when(repository).deleteById(1L);

        // Act
        service.deleteCampaign(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }

}
