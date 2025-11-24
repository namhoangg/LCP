package com.lcp.quote.service;

import com.lcp.common.PageResponse;
import com.lcp.quote.dto.LandingPageQuoteRequestDto;
import com.lcp.quote.dto.QuoteCreateRequestClient;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.dto.QuoteResponseDto;

public interface QuoteRequestService {
    void create(QuoteCreateRequestClient createDto);

    void update(Long id, QuoteCreateRequestClient updateDto);

    // QuoteRequestResponseDto detail(Long id);

    // void delete(Long id);

    PageResponse<QuoteResponseDto> list(QuoteListRequest request);

    void requestQuote(LandingPageQuoteRequestDto request);
}
