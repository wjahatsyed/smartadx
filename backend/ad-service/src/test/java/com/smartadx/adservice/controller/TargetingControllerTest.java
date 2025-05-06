package com.smartadx.adservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.dto.ClickRequest;
import com.smartadx.adservice.service.TargetingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TargetingController.class)
@Import(TestTargetingServiceConfig.class)
class TargetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TargetingService targetingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void trackClick_shouldReturnOk() throws Exception {
        ClickRequest request = new ClickRequest();
        request.setCampaignId(42L);
        request.setUserId("user123");
        request.setLocation("Karachi");
        request.setIp("192.168.0.1");

        mockMvc.perform(post("/api/click")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(targetingService).trackClick(any());
    }
}
