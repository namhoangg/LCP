package com.lcp.quote.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.lcp.client.dto.ClientResponseDto;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.rate.dto.GoodKindResponse;
import com.lcp.setting.dto.UnlocoResponseDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QuoteResponseDto {
    private Long id;
    private Long clientId;
    private Long providerId;
    private Long originId;
    private Long destinationId;
    private Long goodKindId;
    private String note;
    private String goodDescription;
    private LocalDate etd;
    private LocalDate eta;
    private List<String> containerTypeIds;
    private List<String> serviceChargeIds;
    private QuoteStatus quoteStatus;
    private String code;
    private LocalDate validUntil;

    private ClientResponseDto client;
    private UnlocoResponseDto origin;
    private UnlocoResponseDto destination;
    private ProviderResponseDto provider;
    private GoodKindResponse goodKind;

    private JsonNode cargoChargeMarkup;
    private JsonNode serviceChargeMarkup;
    private JsonNode cargoChargePrice;

    private JsonNode cargoChargePriceForClient;
    private JsonNode serviceChargeForClient;

    private Long estimatedTransitTime;
}
