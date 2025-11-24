package com.lcp.quote.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.lcp.common.enumeration.ShipmentMode;
import com.lcp.common.enumeration.ShipmentType;
import com.lcp.common.enumeration.TransportType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class QuoteCreateStaffDto {

    @NotNull
    private Long clientId;

    @NotNull
    private Long originId;

    @NotNull
    private Long destinationId;

    @NotNull
    private Long goodKindId;
    private String goodDescription;
    private String note;
    private Boolean isDraft;

    @NotNull
    private LocalDate etd;

    @NotNull
    private LocalDate eta;

    @NotNull
    private List<String> containerTypeIds;

    private List<String> serviceChargeIds;

    private Long providerId;
    private LocalDate validUntil;

    private JsonNode cargoChargeMarkup;
    private JsonNode serviceChargeMarkup;
    private JsonNode cargoChargePrice;
}
