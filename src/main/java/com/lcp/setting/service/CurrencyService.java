package com.lcp.setting.service;

import com.lcp.common.PageResponse;
import com.lcp.setting.dto.CurrencyListRequest;
import com.lcp.setting.dto.CurrencyResponseDto;

public interface CurrencyService {
    PageResponse<CurrencyResponseDto> list(CurrencyListRequest request);

    CurrencyResponseDto detail(Long id);
}
