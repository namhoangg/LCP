package com.lcp.quote.dto;

import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.rate.dto.ChargeTypeResponseDto;
import com.lcp.setting.dto.CurrencyResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotePriceDetailResponseDto {
    private Long id;
    private Long quoteId;
    private Long chargeTypeId;
    private Long providerId;
    private Long quantity;
    private Long currencyId;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private String description;

    private ChargeTypeResponseDto chargeType;
    private ProviderResponseDto provider;
    private CurrencyResponseDto currency;
}
