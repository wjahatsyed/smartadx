package com.smartadx.adservice.service;

import com.smartadx.adservice.dto.ClickRequest;
import com.smartadx.adservice.producer.AdEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class TargetingServiceTest {

    @Mock
    private CampaignCacheService cacheService;

    @Mock
    private AdEventProducer adEventProducer;

    @InjectMocks
    private TargetingService targetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void trackClick_shouldSendClickAdEvent() {
        // Arrange
        ClickRequest request = new ClickRequest();
        request.setCampaignId(42L);
        request.setUserId("user123");
        request.setLocation("Karachi");
        request.setIp("192.168.0.1");

        // Act
        targetingService.trackClick(request);

        // Assert
        verify(adEventProducer, times(1)).sendAdEvent(argThat(event ->
                event.getCampaignId().equals(42L)
                        && event.getType().equals("CLICK")
                        && event.getUserId().equals("user123")
                        && event.getLocation().equals("Karachi")
                        && event.getIp().equals("192.168.0.1")
                        && event.getTimestamp() != null
        ));
    }
}
