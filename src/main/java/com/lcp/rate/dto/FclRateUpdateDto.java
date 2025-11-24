package com.lcp.rate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class FclRateUpdateDto {
    private Long transitTime;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String remark;
    private List<FclRateDetailUpdateDto> details;
}
