package com.smartadx.adservice.controller;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.service.AdCampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ad-campaigns")
public class AdCampaignController {

    private final AdCampaignService adCampaignService;

    public AdCampaignController(AdCampaignService adCampaignService) {
        this.adCampaignService = adCampaignService;
    }

    @GetMapping
    public List<AdCampaign> getAllCampaigns() {
        return adCampaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdCampaign> getCampaignById(@PathVariable Long id) {
        AdCampaign campaign = adCampaignService.getCampaignById(id);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping
    public ResponseEntity<AdCampaign> createCampaign(@RequestBody AdCampaign campaign) {
        AdCampaign created = adCampaignService.createCampaign(campaign);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdCampaign> updateCampaign(@PathVariable Long id, @RequestBody AdCampaign campaign) {
        AdCampaign updated = adCampaignService.updateCampaign(id, campaign);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        adCampaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}

