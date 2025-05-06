package com.smartadx.adservice.dto;

import com.smartadx.adservice.model.AdCampaign.AdType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdCampaignDTO implements Serializable {

    private Long id;

    private String advertiser;

    private String content;

    private AdType type; // TEXT, BANNER, VIDEO_URL

    private String location;

    private String ageGroup;

    private List<String> interestTags;

    private Double budget;
    private Double remainingBudget;

    private Integer impressions;
    private Integer clicks;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdAt;
}
