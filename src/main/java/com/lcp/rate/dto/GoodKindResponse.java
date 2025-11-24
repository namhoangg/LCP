package com.lcp.rate.dto;

import lombok.Data;

@Data
public class GoodKindResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isRefrigerated;
    private Boolean isDefault;
}
