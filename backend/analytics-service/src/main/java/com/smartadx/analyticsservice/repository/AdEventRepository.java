package com.smartadx.analyticsservice.repository;

import com.smartadx.analyticsservice.model.AdEvent;
import com.smartadx.analyticsservice.model.AdEvent.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdEventRepository extends JpaRepository<AdEvent, Long> {
    List<AdEvent> findByCampaignIdAndType(Long campaignId, EventType type);
}
