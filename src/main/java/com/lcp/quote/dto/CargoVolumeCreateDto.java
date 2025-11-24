package com.lcp.quote.dto;

import lombok.Data;

@Data
public class CargoVolumeCreateDto {
    private Boolean isFCL;
    // FCL
    private Long totalCont20dc;
    private Long totalCont40dc;
    private Long totalCont20rf;
    private Long totalCont40rf;
    private Long totalCont20hc;
    private Long totalCont40hc;
    private Long totalCont45hc;
    // LCL
    private Long totalVolume;
    private Long totalWeight;
}
