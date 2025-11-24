package com.lcp.invoice.service;

import com.lcp.common.PageResponse;
import com.lcp.invoice.dto.DebitNoteCreateDto;
import com.lcp.invoice.dto.DebitNoteListRequest;
import com.lcp.invoice.dto.DebitNoteResponseDto;
import com.lcp.invoice.dto.DebitNoteUpdateDto;

public interface DebitNoteService {
    DebitNoteResponseDto create(DebitNoteCreateDto createDto);

    DebitNoteResponseDto update(Long id, DebitNoteUpdateDto updateDto);

    DebitNoteResponseDto detail(Long id);

    void delete(Long id);

    PageResponse<DebitNoteResponseDto> list(DebitNoteListRequest request);
}
