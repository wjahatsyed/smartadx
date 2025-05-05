package com.smartadx.adservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetedAdResponse {
    private Long campaignId;
    private String content;
    private double score;
}
