package com.lcp.invoice.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.invoice.common.ApiInvoiceMessage;
import com.lcp.invoice.dto.InvoiceCreateDto;
import com.lcp.invoice.dto.InvoiceListRequest;
import com.lcp.invoice.dto.InvoiceResponseDto;
import com.lcp.invoice.dto.InvoiceUpdateStatusDto;
import com.lcp.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping(value = "")
    public Response<InvoiceResponseDto> create(@Valid @RequestBody InvoiceCreateDto createDto) {
        return Response.success(invoiceService.create(createDto), ApiInvoiceMessage.INVOICE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}/status")
    public Response<InvoiceResponseDto> updateStatus(@PathVariable Long id, @Valid @RequestBody InvoiceUpdateStatusDto updateDto) {
        return Response.success(invoiceService.updateStatus(id, updateDto), ApiInvoiceMessage.INVOICE_STATUS_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<InvoiceResponseDto> detail(@PathVariable Long id) {
        return Response.success(invoiceService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return Response.success(ApiInvoiceMessage.INVOICE_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<InvoiceResponseDto>> list(InvoiceListRequest request) {
        return Response.success(invoiceService.list(request));
    }
}
