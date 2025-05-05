package com.smartadx.adservice.controller;

import com.smartadx.adservice.dto.TargetedAdResponse;
import com.smartadx.adservice.dto.UserProfileDto;
import com.smartadx.adservice.service.TargetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/target")
@RequiredArgsConstructor
public class TargetingController {

    private final TargetingService targetingService;

    @PostMapping
    public TargetedAdResponse getAd(@RequestBody UserProfileDto userProfile) {
        return targetingService.getBestMatchingAd(userProfile);
    }

    @GetMapping("/refresh-cache")
    public ResponseEntity<String> refreshCache() {
        targetingService.cacheActiveCampaigns();
        return ResponseEntity.ok("Cache refreshed successfully");
    }
}
