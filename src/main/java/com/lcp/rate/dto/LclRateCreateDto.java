package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LclRateCreateDto {
    private TransportType transportType = TransportType.ROAD_LCL;
    private Long providerId;
    private Long originId;
    private Long destinationId;
    private Long transitTime;
    private Long kindId;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long currencyId;
    private String remark;
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal minWeight;

    private BigDecimal maxWeight; // null if no limit

    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal minVolume;

    private BigDecimal maxVolume; // null if no limit

    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal pricePerWeight;

    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal pricePerVolume;

    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal minimumCharge;

    private BigDecimal volumetricFactor = BigDecimal.valueOf(333);
}
