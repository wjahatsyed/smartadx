package com.smartadx.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdCampaignDto {
    private Long id;
    private String content;
    private String type; // TEXT, BANNER, VIDEO_URL

    private String location;
    private String ageGroup;
    private List<String> interestTags;

    private String startDate;
    private String endDate;

    private BigDecimal budget;
    private BigDecimal remainingBudget;

    private Integer impressions;
    private Integer clicks;

    @Override
    public String toString() {
        return "AdCampaignDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", ageGroup='" + ageGroup + '\'' +
                ", interestTags=" + interestTags +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget=" + budget +
                ", remainingBudget=" + remainingBudget +
                ", impressions=" + impressions +
                ", clicks=" + clicks +
                '}';
    }
}
