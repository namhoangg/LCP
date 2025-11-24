package com.lcp.quote.service;

import com.lcp.common.PageResponse;
import com.lcp.quote.dto.QuoteCreateRequest;
import com.lcp.quote.dto.QuoteCreateStaffDto;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.dto.QuoteResponseDto;

import java.util.List;

public interface QuoteService {
    void create(Long id, QuoteCreateRequest createDto);

    QuoteResponseDto update(Long id, QuoteCreateStaffDto updateDto);

    // QuoteResponseDto detail(Long id);

    // void delete(Long id);

    PageResponse<QuoteResponseDto> list(QuoteListRequest request);

    void staffCreate(QuoteCreateStaffDto createDto);

    // void updateStatus(QuoteUpdateStatusDto updateStatusDto);

    void accept(Long id, Long providerId, Long rateId);

    void reject(Long id);

    // void changeRequest(Long id, QuoteRejectDto changeRequestDto);

    List<QuoteResponseDto> clientQuoteList(Long id);

    // BigDecimal getTotalFreightBaseAmount(Quote quote);

    // BigDecimal getTotalFreightAmount(Quote quote);
}
