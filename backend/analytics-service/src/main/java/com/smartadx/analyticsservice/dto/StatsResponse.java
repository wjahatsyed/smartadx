package com.smartadx.analyticsservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {
    private Long campaignId;
    private long impressions;
    private long clicks;
    private double ctr;
}
