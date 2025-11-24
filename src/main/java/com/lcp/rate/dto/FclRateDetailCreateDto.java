package com.lcp.rate.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FclRateDetailCreateDto {
    private Long containerTypeId;
    private BigDecimal rate;
}
