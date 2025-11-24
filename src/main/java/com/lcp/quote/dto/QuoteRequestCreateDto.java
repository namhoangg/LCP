package com.lcp.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.common.enumeration.ShipmentMode;
import com.lcp.common.enumeration.ShipmentType;
import com.lcp.common.enumeration.TransportType;
import lombok.Data;

@Data
public class QuoteRequestCreateDto {
    @JsonIgnore
    private Long clientId;
    private Boolean isRequest;
    private TransportType transportType;
    private ShipmentMode shipmentMode;
    private ShipmentType shipmentType;
    private Long originId;
    private Long destinationId;
    private String incoterm;
    private String note = "";
    private Boolean isDraft;

    private CargoVolumeCreateDto cargoVolume;
}
