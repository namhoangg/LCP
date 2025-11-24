package com.lcp.markup.dto;

import lombok.Data;

import java.util.List;

@Data
public class PriceMarkupUpdateDto {
    private String name;
    private String description;

    List<PriceMarkupDetailUpdateDto> priceMarkupDetails;
}
