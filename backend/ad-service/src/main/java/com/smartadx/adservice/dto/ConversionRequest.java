package com.smartadx.adservice.dto;

import lombok.Data;

@Data
public class ConversionRequest {
    private Long campaignId;
    private String userId;
    private String location;
    private String ip;
}
