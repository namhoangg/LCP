package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.setting.dto.CurrencyResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServiceChargeResponseDto {
    private Long id;
    private TransportType transportType;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long chargeTypeId;
    private Long currencyId;
    private ChargeTypeResponseDto chargeType;
    private CurrencyResponseDto currency;
    private BigDecimal price;
    private ProviderResponseDto provider;
}
