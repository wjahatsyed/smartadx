package com.smartadx.adservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdEventDto {
    private Long campaignId;
    private String type; // IMPRESSION, CLICK, CONVERSION
    private String userId;
    private String location;
    private String ip;
    private LocalDateTime timestamp;
}
