package com.lcp.provider.service;

import com.lcp.common.PageResponse;
import com.lcp.provider.dto.*;

import java.util.List;

public interface ProviderService {
    ProviderResponseDto create(ProviderCreateDto createDto);

    ProviderResponseDto update(Long id, ProviderUpdateDto updateDto);

    ProviderResponseDto detail(Long id);

    void delete(Long id);

    PageResponse<ProviderResponseDto> list(ProviderListRequest request);

    List<ProviderByQuoteResponse> getByQuote(GetByQuoteRequest request);
}
