package com.lcp.client.service;

import com.lcp.client.dto.ClientCreateDto;
import com.lcp.client.dto.ClientListRequest;
import com.lcp.client.dto.ClientResponseDto;
import com.lcp.client.dto.ClientUpdateDto;
import com.lcp.common.PageResponse;

public interface ClientService {
    ClientResponseDto create(ClientCreateDto createDto);
    ClientResponseDto update(Long id, ClientUpdateDto updateDto);
    ClientResponseDto detail(Long id);
    Long getCurrentClientId();
    void delete(Long id);
    PageResponse<ClientResponseDto> list(ClientListRequest request);
}
