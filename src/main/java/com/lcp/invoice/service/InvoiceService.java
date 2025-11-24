package com.lcp.invoice.service;

import com.lcp.common.PageResponse;
import com.lcp.invoice.dto.InvoiceCreateDto;
import com.lcp.invoice.dto.InvoiceListRequest;
import com.lcp.invoice.dto.InvoiceResponseDto;
import com.lcp.invoice.dto.InvoiceUpdateStatusDto;

public interface InvoiceService {
    InvoiceResponseDto create(InvoiceCreateDto createDto);

    InvoiceResponseDto updateStatus(Long id, InvoiceUpdateStatusDto paymentStatus);

    InvoiceResponseDto detail(Long id);

    void delete(Long id);

    PageResponse<InvoiceResponseDto> list(InvoiceListRequest request);
}
