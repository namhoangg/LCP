package com.lcp.markup.dto;

import lombok.Data;

import java.util.List;

@Data
public class PriceMarkupResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<PriceMarkupDetailResponseDto> priceMarkupDetails;
}
