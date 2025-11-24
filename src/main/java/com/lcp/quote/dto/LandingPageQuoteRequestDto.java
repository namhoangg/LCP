package com.lcp.quote.dto;

import com.lcp.client.dto.ClientCreateDto;
import lombok.Data;

@Data
public class LandingPageQuoteRequestDto {
    private ClientCreateDto client;
    private QuoteCreateRequest quoteRequest;
}
