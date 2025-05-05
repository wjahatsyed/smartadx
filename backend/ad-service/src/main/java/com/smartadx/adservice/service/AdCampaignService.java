package com.smartadx.adservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdCampaignService {

    private final AdCampaignRepository adCampaignRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public List<AdCampaign> getAllCampaigns() {
        return adCampaignRepository.findAll();
    }

    public AdCampaign getCampaignById(Long id) {
        return adCampaignRepository.findById(id).orElseThrow(() -> new RuntimeException("Ad Campaign not found with id: " + id));
    }

    public AdCampaign createCampaign(AdCampaign campaign) {
        AdCampaign savedCampaign = adCampaignRepository.save(campaign);
        // Publish message to RabbitMQ when a new campaign is created
        try {
            String campaignJson = objectMapper.writeValueAsString(savedCampaign);
            rabbitTemplate.convertAndSend("adCampaignQueue", campaignJson);
            System.out.println("📤 Sent message to queue: " + campaignJson);
        } catch (Exception e) {
            System.err.println("❌ Failed to send message: " + e.getMessage());
        }
        return savedCampaign;
    }

    public AdCampaign updateCampaign(Long id, AdCampaign updatedCampaign) {
        AdCampaign existing = getCampaignById(id);
        existing.setContent(updatedCampaign.getContent());
        existing.setStartDate(updatedCampaign.getStartDate());
        existing.setEndDate(updatedCampaign.getEndDate());
        existing.setBudget(updatedCampaign.getBudget());

        AdCampaign savedCampaign = adCampaignRepository.save(existing);
        try {
            String campaignJson = objectMapper.writeValueAsString(savedCampaign);
            rabbitTemplate.convertAndSend("adCampaignQueue", campaignJson);
            System.out.println("📤 Sent message to queue: " + campaignJson);
        } catch (Exception e) {
            System.err.println("❌ Failed to send message: " + e.getMessage());
        }
        return savedCampaign;
    }

    public void deleteCampaign(Long id) {
        if (!adCampaignRepository.existsById(id)) {
            throw new RuntimeException("Ad Campaign not found with id: " + id);
        }
        adCampaignRepository.deleteById(id);
    }

    // Helper method to send the message to RabbitMQ queue
    private void sendMessageToQueue(AdCampaign campaign) {
        // Send message to the "adCampaignQueue" in RabbitMQ
        rabbitTemplate.convertAndSend("adCampaignQueue", campaign);
    }
}
