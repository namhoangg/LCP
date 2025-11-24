package com.lcp.rate.service;

import com.lcp.common.PageResponse;
import com.lcp.common.dto.BaseListRequest;
import com.lcp.rate.dto.LclRateCreateDto;
import com.lcp.rate.dto.LclRateResponseDto;
import com.lcp.rate.dto.LclRateUpdateDto;

public interface LclRateService {
    void create(LclRateCreateDto createDto);

    void update(LclRateUpdateDto updateDto);

    LclRateResponseDto detail(Long id);

    void delete(Long id);

    PageResponse<LclRateResponseDto> list(BaseListRequest request);
}
