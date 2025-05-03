package com.smartadx.adservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AdCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String advertiser;

    private String content; // e.g., banner URL, text

    private String targetingKeywords; // e.g., json string or comma-separated

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double budget;

    private Integer impressions;

    private LocalDateTime createdAt;
}
