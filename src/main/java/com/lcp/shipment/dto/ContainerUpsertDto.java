package com.lcp.shipment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Builder
@Data
public class ContainerUpsertDto {
    private Long id;
    private String containerNumber;
    private Long containerTypeId;
    @Min(-100)
    @Max(100)
    private Integer minTemp;
    @Min(-100)
    @Max(100)
    private Integer maxTemp;

    private String sealNumber;
    @DecimalMin(value = "0.00")
    private BigDecimal netWeight;
    @DecimalMin(value = "0.00")
    private BigDecimal grossWeight;
    @DecimalMin(value = "0.00")
    private BigDecimal volume;
    private String note;
}
