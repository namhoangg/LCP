package com.lcp.markup.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceMarkupDetailUpdateDto {
    private Long id;
    private BigDecimal markupRate;
}
