package com.smartadx.adservice.controller;

import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.service.AdServingService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/serve")
public class AdServingController {

    private final AdServingService adServingService;

    public AdServingController(AdServingService adServingService) {
        this.adServingService = adServingService;
    }

    @GetMapping
    public Optional<AdCampaign> serveAd(@RequestParam("keywords") String keywordCsv) {
        List<String> keywords = Arrays.stream(keywordCsv.split(","))
                .map(String::trim)
                .toList();

        return adServingService.serveAd(keywords);
    }
}
