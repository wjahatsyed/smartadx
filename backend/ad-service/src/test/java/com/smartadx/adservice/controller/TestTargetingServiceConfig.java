package com.smartadx.adservice.controller;

import com.smartadx.adservice.service.TargetingService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestTargetingServiceConfig {

    @Bean
    public TargetingService targetingService() {
        return Mockito.mock(TargetingService.class);
    }
}
