package com.lcp.rate.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ContainerTypeResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal tareWeight;
    private BigDecimal maxWeight;
    private BigDecimal maxVolume;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private Boolean isRefrigerated;
}
