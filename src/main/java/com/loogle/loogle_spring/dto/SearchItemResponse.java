package com.loogle.loogle_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Immutable;

import java.util.List;

@Data
@AllArgsConstructor
@Immutable
public class SearchItemResponse {
    private List<Long> productIds;
    private List<Double> similarities;
    private List<ProductResponse> products;
}