package com.lcp.rate.service;

import com.lcp.common.PageResponse;
import com.lcp.rate.dto.FclRateCreateDto;
import com.lcp.rate.dto.FclRateListRequest;
import com.lcp.rate.dto.FclRateResponseDto;
import com.lcp.rate.dto.FclRateUpdateDto;

public interface FclRateService {
    FclRateResponseDto create(FclRateCreateDto createDto);

    FclRateResponseDto update(Long id, FclRateUpdateDto updateDto);

    FclRateResponseDto detail(Long id);

    void delete(Long id);

    PageResponse<FclRateResponseDto> list(FclRateListRequest request);

    FclRateResponseDto getByProvider(Long providerId);
}
