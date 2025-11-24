package com.lcp.exchangerate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExchangeRateResponseDto {
    private BigDecimal transferRate;
    private LocalDate createdDate;
}
