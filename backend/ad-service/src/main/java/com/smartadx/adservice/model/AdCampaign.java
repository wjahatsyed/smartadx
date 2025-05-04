package com.smartadx.adservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    private String content; // e.g., banner URL, video URL, text content

    @Enumerated(EnumType.STRING)
    private AdType type; // TEXT, BANNER, VIDEO_URL

    // Targeting fields
    private String location;         // e.g., "USA", "Pakistan"
    private String ageGroup;         // e.g., "18-25"

    @ElementCollection
    private List<String> interestTags; // e.g., ["sports", "travel"]

    private Double budget;
    private Double remainingBudget; // for real-time depletion tracking

    private Integer impressions;
    private Integer clicks;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private LocalDateTime createdAt;

    public enum AdType {
        TEXT, BANNER, VIDEO_URL
    }
}
