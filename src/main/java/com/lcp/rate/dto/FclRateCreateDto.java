package com.lcp.rate.dto;

import com.lcp.common.enumeration.TransportType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FclRateCreateDto {
    private Long providerId;
    private Long originId;
    private Long destinationId;
    private Long transitTime;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long currencyId;
    private String remark;
    private List<FclRateDetailCreateDto> details;
}
