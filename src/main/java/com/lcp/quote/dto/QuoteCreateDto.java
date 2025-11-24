package com.lcp.quote.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class QuoteCreateDto {
    private Long quoteRequestId;
    private Long providerId;
    private String staffNote = "";
    private LocalDate validUntil;
    private BigDecimal profitRate;
    private Boolean isDraft;
    private Long currencyId;

    private List<QuotePriceDetailCreateDto> quotePriceDetails;

    private Long rateId;
}
