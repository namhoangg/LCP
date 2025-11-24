package com.lcp.rate.service;

import com.lcp.common.PageResponse;
import com.lcp.rate.dto.ChargeTypeCreateDto;
import com.lcp.rate.dto.ChargeTypeListRequest;
import com.lcp.rate.dto.ChargeTypeResponseDto;
import com.lcp.rate.dto.ChargeTypeUpdateDto;

public interface ChargeTypeService {
    ChargeTypeResponseDto create(ChargeTypeCreateDto createDto);
    ChargeTypeResponseDto update(Long id, ChargeTypeUpdateDto updateDto);
    ChargeTypeResponseDto detail(Long id);
    void delete(Long id);
    PageResponse<ChargeTypeResponseDto> list(ChargeTypeListRequest request);
}
