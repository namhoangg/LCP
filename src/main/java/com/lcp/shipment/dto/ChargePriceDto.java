package com.lcp.shipment.dto;

import lombok.Data;

@Data
public class ChargePriceDto {
    private String chargeTypeName;
    private Long quantity;
    private Double totalPrice;
    private String currencyCode;
} 