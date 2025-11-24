package com.lcp.shipment.dto;

import com.lcp.common.enumeration.ShipmentStatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ShipmentStatusResponseDto {
    private Long id;
    private ShipmentStatusEnum shipmentStatus;
    private LocalDate eta;
    private LocalDate ata;
    private String note;
}
