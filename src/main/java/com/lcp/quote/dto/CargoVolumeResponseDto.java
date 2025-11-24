package com.lcp.quote.dto;

import com.lcp.rate.dto.FclRateResponseDto;
import com.lcp.rate.dto.LclRateResponseDto;
import com.lcp.shipment.dto.ContainerPriceDto;
import lombok.Data;

@Data
public class CargoVolumeResponseDto {
    private Long id;
    private Long quoteId;
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
    private Double totalVolume;
    private Double totalWeight;

    private Long fclRateId;
    private Long lclRateId;
    private FclRateResponseDto fclRate;
    private LclRateResponseDto lclRate;

    // Calculated prices
    private ContainerPriceDto cont20dcPrice;
    private ContainerPriceDto cont40dcPrice;
    private ContainerPriceDto cont20rfPrice;
    private ContainerPriceDto cont40rfPrice;
    private ContainerPriceDto cont20hcPrice;
    private ContainerPriceDto cont40hcPrice;
    private ContainerPriceDto cont45hcPrice;
    private Double totalLclPrice;
    private Double baseLclPrice;
    private String lclCurrencyCode;
}
