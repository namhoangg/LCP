package com.lcp.shipment.dto;

import lombok.Data;

@Data
public class ContainerPriceDto {
    private Long quantity;
    private Double pricePerUnit;
    private Double totalPrice;
    private String currencyCode;
} 