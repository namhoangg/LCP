package com.lcp.markup.service;

import com.lcp.common.PageResponse;
import com.lcp.markup.dto.PriceMarkupCreateDto;
import com.lcp.markup.dto.PriceMarkupListRequest;
import com.lcp.markup.dto.PriceMarkupResponseDto;
import com.lcp.markup.dto.PriceMarkupUpdateDto;

public interface PriceMarkupService {
    PriceMarkupResponseDto create(PriceMarkupCreateDto createDto);
    PriceMarkupResponseDto update(Long id, PriceMarkupUpdateDto createDto);
    PriceMarkupResponseDto detail(Long id);
    void delete(Long id);
    PageResponse<PriceMarkupResponseDto> list(PriceMarkupListRequest request);
}
