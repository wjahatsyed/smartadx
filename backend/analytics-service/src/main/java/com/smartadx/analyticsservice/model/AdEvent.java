package com.smartadx.analyticsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long campaignId;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String userId;

    private String location; // Optional: helpful for analysis
    private String ip;       // Optional: for deeper insights

    private LocalDateTime timestamp;

    public enum EventType {
        IMPRESSION, CLICK, CONVERSION
    }
}
