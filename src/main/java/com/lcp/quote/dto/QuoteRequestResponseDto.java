package com.lcp.quote.dto;

import com.lcp.client.dto.ClientResponseDto;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.common.enumeration.ShipmentMode;
import com.lcp.common.enumeration.ShipmentType;
import com.lcp.common.enumeration.TransportType;
import com.lcp.setting.dto.UnlocoResponseDto;
import lombok.Data;

@Data
public class QuoteRequestResponseDto {
    private Long id;
    private Long clientId;
    private Boolean isRequest;
    private TransportType transportType;
    private ShipmentMode shipmentMode;
    private ShipmentType shipmentType;
    private Long originId;
    private Long destinationId;
    private Long cargoVolumeId;
    private String incoterm;
    private String note;

    private ClientResponseDto client;
    private UnlocoResponseDto origin;
    private UnlocoResponseDto destination;
    private CargoVolumeResponseDto cargoVolume;

    private QuoteStatus quoteStatus;
    private String code;
}
