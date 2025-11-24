package com.lcp.invoice.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.invoice.common.ApiInvoiceMessage;
import com.lcp.invoice.dto.DebitNoteCreateDto;
import com.lcp.invoice.dto.DebitNoteListRequest;
import com.lcp.invoice.dto.DebitNoteResponseDto;
import com.lcp.invoice.dto.DebitNoteUpdateDto;
import com.lcp.invoice.service.DebitNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/debit-note")
@RequiredArgsConstructor
public class DebitNoteController {
    private final DebitNoteService debitNoteService;

    @PostMapping(value = "")
    public Response<DebitNoteResponseDto> create(@Valid @RequestBody DebitNoteCreateDto createDto) {
        return Response.success(debitNoteService.create(createDto), ApiInvoiceMessage.DEBIT_NOTE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<DebitNoteResponseDto> update(@PathVariable Long id, @Valid @RequestBody DebitNoteUpdateDto updateDto) {
        return Response.success(debitNoteService.update(id, updateDto), ApiInvoiceMessage.DEBIT_NOTE_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<DebitNoteResponseDto> detail(@PathVariable Long id) {
        return Response.success(debitNoteService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        debitNoteService.delete(id);
        return Response.success(ApiInvoiceMessage.DEBIT_NOTE_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<DebitNoteResponseDto>> list(DebitNoteListRequest request) {
        return Response.success(debitNoteService.list(request));
    }
}
