package com.lcp.rate.dto;

import java.math.BigDecimal;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ContainerTypeUpdate {

    @NotNull
    private Long id;

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
