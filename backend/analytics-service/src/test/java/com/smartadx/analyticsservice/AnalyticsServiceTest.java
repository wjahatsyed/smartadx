package com.smartadx.analyticsservice.service;

import com.smartadx.analyticsservice.model.AdEvent;
import com.smartadx.analyticsservice.model.AdEvent.EventType;
import com.smartadx.analyticsservice.repository.AdEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    @Mock
    private AdEventRepository repository;

    @InjectMocks
    private AnalyticsServiceImpl service;

    private AdEvent sampleEvent;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        sampleEvent = AdEvent.builder()
                .id(1L)
                .campaignId(100L)
                .type(EventType.CLICK)
                .userId("user123")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldRecordEvent() {
        when(repository.save(any())).thenReturn(sampleEvent);

        AdEvent result = service.recordEvent(sampleEvent);

        assertThat(result.getCampaignId()).isEqualTo(100L);
    }

    @Test
    void shouldReturnEventCountByType() {
        when(repository.findByCampaignIdAndType(100L, EventType.CLICK)).thenReturn(List.of(sampleEvent));

        long count = service.getEventCountByCampaignAndType(100L, EventType.CLICK);

        assertThat(count).isEqualTo(1);
    }
}
