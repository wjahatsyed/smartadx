package com.smartadx.adservice.dto;

import lombok.Data;

@Data
public class ClickRequest {
    private Long campaignId;
    private String userId;
    private String location;
    private String ip;
}
