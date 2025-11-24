package com.lcp.shipment.dto;

import com.lcp.common.enumeration.ShipmentStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ShipmentStatusCreateDto {
    @NotNull
    private ShipmentStatusEnum shipmentStatus;
    private LocalDate eta;
    private LocalDate ata;
    private String note;
}
