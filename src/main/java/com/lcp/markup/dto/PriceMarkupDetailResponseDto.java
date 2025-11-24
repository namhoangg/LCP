package com.lcp.markup.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceMarkupDetailResponseDto {
    private Long id;
    private BigDecimal markupRate;
    private Long priceMarkupId;
    private Long containerTypeId;
    private String containerTypeName;
}
