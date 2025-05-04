package com.smartadx.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AdCampaignDto {
    private Long id;
    private String content;
    private String startDate;
    private String endDate;
    private BigDecimal budget;

    // Getters and Setters

    @Override
    public String toString() {
        return "AdCampaignDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget=" + budget +
                '}';
    }
}
