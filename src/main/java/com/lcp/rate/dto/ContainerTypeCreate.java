package com.lcp.rate.dto;

import lombok.Data;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ContainerTypeCreate {

    @NotEmpty
    @NotNull
    private String name;
    private String description;

    @NotNull
    private BigDecimal tareWeight;
    @NotNull
    private BigDecimal maxWeight;
    @NotNull
    private BigDecimal maxVolume;
    @NotNull
    private BigDecimal height;
    @NotNull
    private BigDecimal width;
    @NotNull
    private BigDecimal length;
    @NotNull
    private Boolean isRefrigerated = false;
}
