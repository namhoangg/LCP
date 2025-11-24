package com.lcp.invoice.mapper;

import com.lcp.invoice.dto.DebitNoteCreateDto;
import com.lcp.invoice.dto.DebitNoteResponseDto;
import com.lcp.invoice.entity.DebitNote;
import com.lcp.util.MapUtil;

public class DebitNoteMapper {
    public static DebitNote createEntity(DebitNoteCreateDto createDto) {
        DebitNote debitNote = new DebitNote();
        MapUtil.copyProperties(createDto, debitNote);
        return debitNote;
    }

    public static DebitNoteResponseDto createResponse(DebitNote entity) {
        DebitNoteResponseDto responseDto = new DebitNoteResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
