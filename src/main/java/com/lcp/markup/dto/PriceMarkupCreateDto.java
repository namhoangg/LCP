package com.lcp.markup.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PriceMarkupCreateDto {
    @NotNull
    private String name;
    private String description;

    List<PriceMarkupDetailCreateDto> priceMarkupDetails;
}
