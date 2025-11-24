package com.lcp.markup.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceMarkupDetailCreateDto {
    private BigDecimal markupRate;
    private Long containerTypeId;
}
