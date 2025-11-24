package com.lcp.quote.dto;

import com.lcp.common.enumeration.QuoteStatus;
import lombok.Data;

@Data
public class QuoteUpdateStatusDto {
    private Long quoteId;
    private QuoteStatus quoteStatus;
    private String reason;
}
