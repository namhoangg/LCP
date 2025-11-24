package com.lcp.shipment.dto;

import com.lcp.quote.dto.QuoteResponseDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ShipmentResponseDto {
    private Long id;
    private String code;
    private Long quoteId;
    private LocalDate etd;
    private LocalDate eta;
    private Long shipmentStatusId;
    private String note;
    private String attachmentIds;

    private QuoteResponseDto quote;
    private ShipmentStatusResponseDto shipmentStatus;
    private List<ContainerResponseDto> containers;
}
