package com.smartadx.adservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartadx.adservice.controller.AdCampaignController;
import com.smartadx.adservice.model.AdCampaign;
import com.smartadx.adservice.service.AdCampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import java.util.Optional;



import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdCampaignControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdCampaignService service;

    @InjectMocks
    private AdCampaignController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AdCampaign sampleCampaign;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        sampleCampaign = AdCampaign.builder()
                .id(1L)
                .advertiser("Nike")
                .content("https://ads.com/banner.jpg")
                .targetingKeywords("sports,shoes")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .budget(2000.0)
                .impressions(1000)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnAllCampaigns() throws Exception {
        when(service.getAllCampaigns()).thenReturn(List.of(sampleCampaign));

        mockMvc.perform(get("/api/campaigns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].advertiser").value("Nike"));
    }

    @Test
    void shouldReturnCampaignById() throws Exception {
        // Mock the service method to return a sample campaign
        when(service.getCampaignById(1L)).thenReturn(sampleCampaign);

        // Perform the GET request to fetch the campaign by ID
        mockMvc.perform(get("/api/campaigns/1"))
                .andExpect(status().isOk())  // Expecting HTTP 200 OK status
                .andExpect(jsonPath("$.content").value("https://ads.com/banner.jpg"));  // Checking if the content matches
    }


    @Test
    void shouldReturnNotFoundIfCampaignNotExists() throws Exception {
        when(service.getCampaignById(2L)).thenThrow(new RuntimeException("Ad Campaign not found with id: 2"));

        mockMvc.perform(get("/api/campaigns/2"))
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateCampaign() throws Exception {
        when(service.createCampaign(any())).thenReturn(sampleCampaign);

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCampaign)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldUpdateCampaign() throws Exception {
        // Mock the updateCampaign service method to return the updated campaign
        when(service.updateCampaign(eq(1L), any())).thenReturn(sampleCampaign);

        // Perform the PUT request to update the campaign
        mockMvc.perform(put("/api/campaigns/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCampaign)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldDeleteCampaign() throws Exception {
        doNothing().when(service).deleteCampaign(1L);

        mockMvc.perform(delete("/api/campaigns/1"))
                .andExpect(status().isNoContent());
    }
}
