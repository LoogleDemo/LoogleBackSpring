package com.loogle.loogle_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandSaveRequest {

    private String brandName;
    private String iconUrl;
    private String bannerUrl;
}