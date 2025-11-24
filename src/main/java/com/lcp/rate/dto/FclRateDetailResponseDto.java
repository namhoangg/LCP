package com.lcp.rate.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FclRateDetailResponseDto {
    private Long id;
    private Long containerTypeId;
    private BigDecimal rate;
    private String containerTypeName;
}