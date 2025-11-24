package com.lcp.rate.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GoodKindCreate {

    @NotEmpty
    @NotNull
    private String name;
    private String description;

    @NotNull
    private Boolean isRefrigerated = false;
}
