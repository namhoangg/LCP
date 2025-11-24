package com.lcp.quote.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import lombok.Data;

@Data
public class QuoteCreateRequest {
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

    // Fix: Initialize with JsonNodeFactory
    private JsonNode cargoChargeMarkup = JsonNodeFactory.instance.arrayNode();
    private JsonNode serviceChargeMarkup = JsonNodeFactory.instance.arrayNode();
    private JsonNode cargoChargePrice = JsonNodeFactory.instance.arrayNode();
}