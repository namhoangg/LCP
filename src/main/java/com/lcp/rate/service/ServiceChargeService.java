package com.lcp.rate.service;

import com.lcp.common.PageResponse;
import com.lcp.rate.dto.ServiceChargeCreateDto;
import com.lcp.rate.dto.ServiceChargeListRequest;
import com.lcp.rate.dto.ServiceChargeResponseDto;
import com.lcp.rate.dto.ServiceChargeUpdateDto;

public interface ServiceChargeService {
    ServiceChargeResponseDto create(ServiceChargeCreateDto createDto);
    ServiceChargeResponseDto update(Long id, ServiceChargeUpdateDto updateDto);
    ServiceChargeResponseDto detail(Long id);
    void delete(Long id);
    PageResponse<ServiceChargeResponseDto> list(ServiceChargeListRequest request);
}
