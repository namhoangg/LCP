package com.lcp.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContainerTypeQuantityDto {
    private Long containerTypeId;
    private Long quantity;
}
