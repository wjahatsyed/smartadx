package com.smartadx.analyticsservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.analyticsservice.model.AdEvent;
import com.smartadx.analyticsservice.repository.AdEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AdEventListener {

    private final ObjectMapper objectMapper;
    private final AdEventRepository adEventRepository;

    public AdEventListener(ObjectMapper objectMapper, AdEventRepository adEventRepository) {
        this.objectMapper = objectMapper;
        this.adEventRepository = adEventRepository;
    }

    @RabbitListener(queues = "adEventQueue")
    public void receiveAdEvent(String message) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(message, Map.class);

        AdEvent event = AdEvent.builder()
                .campaignId(Long.valueOf(payload.get("campaignId").toString()))
                .type(AdEvent.EventType.valueOf(payload.get("type").toString()))
                .userId((String) payload.get("userId"))
                .ip((String) payload.get("ip"))
                .location((String) payload.get("location"))
                .timestamp(LocalDateTime.parse((String) payload.get("timestamp"))) // use formatter if needed
                .build();

        adEventRepository.save(event);
        System.out.println("✅ Stored AdEvent: " + event);
    }
}
