package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.setting.dto.CurrencyResponseDto;
import com.lcp.setting.dto.UnlocoResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FclRateResponseDto {
    private Long id;
    private Long transitTime;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String remark;
    private ProviderResponseDto provider;
    private UnlocoResponseDto origin;
    private UnlocoResponseDto destination;
    private CurrencyResponseDto currency;
    private List<FclRateDetailResponseDto> details;
}
