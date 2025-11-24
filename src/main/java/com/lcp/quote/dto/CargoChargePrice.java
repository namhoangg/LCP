package com.lcp.quote.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CargoChargePrice {
    private Long containerTypeId;
    private BigDecimal basePrice;
    private BigDecimal markup;
    private Long currencyId;
    // For calculation
    private BigDecimal totalPrice;
}
