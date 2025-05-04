package com.smartadx.analyticsservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.analyticsservice.dto.AdCampaignDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CampaignListener {

    private final ObjectMapper objectMapper;

    public CampaignListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "adCampaignQueue")
    public void receiveCampaignMessage(String message) throws JsonProcessingException {
        AdCampaignDto adCampaignDto = objectMapper.readValue(message, AdCampaignDto.class);
        System.out.println("✅ Received Ad Campaign: " + adCampaignDto);
    }
}
