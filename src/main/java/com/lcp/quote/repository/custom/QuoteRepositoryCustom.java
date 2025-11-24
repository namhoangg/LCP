package com.lcp.quote.repository.custom;

import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.entity.Quote;
import org.springframework.data.domain.Page;

public interface QuoteRepositoryCustom {
    Page<Quote> listQuoteRequest(QuoteListRequest request);
}
