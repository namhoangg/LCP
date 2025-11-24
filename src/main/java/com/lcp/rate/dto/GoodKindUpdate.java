package com.lcp.rate.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GoodKindUpdate {
    @NotNull
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    private String description;

    @NotNull
    private Boolean isRefrigerated = false;
}
