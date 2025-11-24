package com.lcp.quote.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotePriceDetailCreateDto {
    private Long quoteId;
    private Long chargeTypeId;
    private Long providerId;
    private Long quantity;
    private Long currencyId;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private String description;
}
