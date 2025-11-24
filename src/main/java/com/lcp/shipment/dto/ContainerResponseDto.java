package com.lcp.shipment.dto;

import com.lcp.rate.dto.ContainerTypeResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContainerResponseDto {
    private Long id;
    private Long shipmentId;
    private String containerNumber;
    private Long containerTypeId;
    private Integer minTemp;
    private Integer maxTemp;

    private String sealNumber;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
    private BigDecimal volume;
    private String note;

    private ContainerTypeResponse containerType;
}
