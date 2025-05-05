package com.smartadx.adservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileDto {
    private String ip;
    private String location;
    private int age;
    private List<String> interests;
}
