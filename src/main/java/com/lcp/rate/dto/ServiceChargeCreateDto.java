package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServiceChargeCreateDto {
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long chargeTypeId;
    private Long currencyId;
    private BigDecimal price;
    private Long providerId;
}
