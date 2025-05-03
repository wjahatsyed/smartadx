package com.smartadx.adservice.service;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.repository.AdCampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdCampaignService {

    private final AdCampaignRepository adCampaignRepository;

    public AdCampaignService(AdCampaignRepository adCampaignRepository) {
        this.adCampaignRepository = adCampaignRepository;
    }

    public List<AdCampaign> getAllCampaigns() {
        return adCampaignRepository.findAll();
    }

    public AdCampaign getCampaignById(Long id) {
        return adCampaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad Campaign not found with id: " + id));
    }

    public AdCampaign createCampaign(AdCampaign campaign) {
        return adCampaignRepository.save(campaign);
    }

    public AdCampaign updateCampaign(Long id, AdCampaign updatedCampaign) {
        AdCampaign existing = getCampaignById(id);
        existing.setContent(updatedCampaign.getContent());
        existing.setStartDate(updatedCampaign.getStartDate());
        existing.setEndDate(updatedCampaign.getEndDate());
        existing.setBudget(updatedCampaign.getBudget());
        return adCampaignRepository.save(existing);
    }

    public void deleteCampaign(Long id) {
        if (!adCampaignRepository.existsById(id)) {
            throw new RuntimeException("Ad Campaign not found with id: " + id);
        }
        adCampaignRepository.deleteById(id);
    }
}
