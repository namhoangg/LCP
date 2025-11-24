package com.lcp.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ServiceChargeMarkup {
    private Long serviceChargeId;
    private BigDecimal markup;
    private BigDecimal basePrice;
    private Long currencyId;
    // For calculation
    private BigDecimal totalPrice;
}
