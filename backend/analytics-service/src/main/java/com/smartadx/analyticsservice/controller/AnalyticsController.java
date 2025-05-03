package com.smartadx.analyticsservice.controller;

import com.smartadx.analyticsservice.model.AdEvent;
import com.smartadx.analyticsservice.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/event")
    public ResponseEntity<AdEvent> recordEvent(@RequestBody AdEvent event) {
        return ResponseEntity.ok(analyticsService.recordEvent(event));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<AdEvent>> getCampaignEvents(@PathVariable Long campaignId) {
        return ResponseEntity.ok(analyticsService.getEventsByCampaign(campaignId));
    }

    @GetMapping("/campaign/{campaignId}/count")
    public ResponseEntity<Long> getCountByType(
            @PathVariable Long campaignId,
            @RequestParam("type") AdEvent.EventType type
    ) {
        return ResponseEntity.ok(analyticsService.getEventCountByCampaignAndType(campaignId, type));
    }
}
