package com.lcp.shipment.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ShipmentCreateDto {
    @NotNull
    private Long quoteId;
    private LocalDate etd;
    private LocalDate eta;
    private String note;
    private String attachmentIds;

}
