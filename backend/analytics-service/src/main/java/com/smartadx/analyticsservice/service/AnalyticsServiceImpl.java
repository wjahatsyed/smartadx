package com.smartadx.analyticsservice.service;

import com.smartadx.analyticsservice.model.AdEvent;
import com.smartadx.analyticsservice.repository.AdEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AdEventRepository repository;

    public AnalyticsServiceImpl(AdEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public AdEvent recordEvent(AdEvent event) {
        event.setTimestamp(event.getTimestamp() == null ? java.time.LocalDateTime.now() : event.getTimestamp());
        return repository.save(event);
    }

    @Override
    public List<AdEvent> getEventsByCampaign(Long campaignId) {
        return repository.findAll().stream()
                .filter(e -> e.getCampaignId().equals(campaignId))
                .toList();
    }

    @Override
    public long getEventCountByCampaignAndType(Long campaignId, AdEvent.EventType type) {
        return repository.findByCampaignIdAndType(campaignId, type).size();
    }
}
