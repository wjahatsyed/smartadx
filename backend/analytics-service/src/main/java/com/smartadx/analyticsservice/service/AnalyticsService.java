package com.smartadx.analyticsservice.service;

import com.smartadx.analyticsservice.model.AdEvent;

import java.util.List;

public interface AnalyticsService {
    AdEvent recordEvent(AdEvent event);
    List<AdEvent> getEventsByCampaign(Long campaignId);
    long getEventCountByCampaignAndType(Long campaignId, AdEvent.EventType type);
}
