package com.smartadx.adservice.service;

import com.smartadx.adservice.model.AdCampaign;

import java.util.List;
import java.util.Optional;

public interface AdServingService {
    Optional<AdCampaign> serveAd(List<String> userKeywords);
}
