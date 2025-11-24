package com.lcp.quote.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
public class QuoteUpdateDto {
    private Long quoteRequestId;
    private Long providerId;
    private String incoterm;
    private String note;
    private Date validUntil;
    private BigDecimal profitRate;
    private Long currencyId;

    private List<QuotePriceDetailUpdateDto> quotePriceDetails;
}
