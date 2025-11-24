package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.setting.dto.CurrencyResponseDto;
import com.lcp.setting.dto.UnlocoResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LclRateResponseDto {
    private Long id;
    private TransportType transportType;
    private Long transitTime;
    private GoodKindResponse kind;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String remark;
    private BigDecimal minWeight;
    private BigDecimal maxWeight; // null if no limit
    private BigDecimal minVolume;
    private BigDecimal maxVolume; // null if no limit
    private BigDecimal pricePerWeight;
    private BigDecimal pricePerVolume;
    private BigDecimal minimumCharge;

    private BigDecimal volumetricFactor = BigDecimal.valueOf(333);

    private ProviderResponseDto provider;
    private UnlocoResponseDto origin;
    private UnlocoResponseDto destination;
    private CurrencyResponseDto currency;
}
