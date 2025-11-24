package com.lcp.setting.dto;

import lombok.Data;

@Data
public class CurrencyResponseDto {
    private Long id;
    private String code;
    private String name;
    private String symbol;
}
