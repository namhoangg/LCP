package com.lcp.quote.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.quote.common.ApiQuoteMessage;
import com.lcp.quote.dto.QuoteCreateRequest;
import com.lcp.quote.dto.QuoteCreateStaffDto;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.dto.QuoteResponseDto;
import com.lcp.quote.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/quote")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    // Create quote from client request
    @PostMapping(value = "/staff-send-quote/{id:-?\\d+}")
    public Response<Void> create(@PathVariable Long id, @Valid @RequestBody QuoteCreateRequest createDto) {
        quoteService.create(id, createDto);
        return Response.success(ApiQuoteMessage.QUOTE_CREATE_SUCCESS);
    }

    // Create quote from client information, not have a request from client before
    @PostMapping(value = "/staff-create")
    public Response<QuoteResponseDto> staffCreate(@Valid @RequestBody QuoteCreateStaffDto createDto) {
        quoteService.staffCreate(createDto);
        return Response.success(ApiQuoteMessage.QUOTE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<QuoteResponseDto> update(@PathVariable Long id, @Valid @RequestBody QuoteCreateStaffDto updateDto) {
        return Response.success(quoteService.update(id, updateDto), ApiQuoteMessage.QUOTE_UPDATE_SUCCESS);
    }

    // @GetMapping(value = "/{id:-?\\d+}")
    // public Response<QuoteResponseDto> detail(@PathVariable Long id) {
    //     return Response.success(quoteService.detail(id));
    // }

    @GetMapping(value = "")
    public Response<PageResponse<QuoteResponseDto>> list(QuoteListRequest request) {
        return Response.success(quoteService.list(request));
    }

    // @DeleteMapping(value = "/{id:-?\\d+}")
    // public Response<Void> delete(@PathVariable Long id) {
    //     quoteService.delete(id);
    //     return Response.success(ApiQuoteMessage.QUOTE_DELETE_SUCCESS);
    // }

    // @PostMapping(value = "/update-status")
    // public Response<Void> updateStatus(@Valid @RequestBody QuoteUpdateStatusDto updateStatusDto) {
    //     quoteService.updateStatus(updateStatusDto);
    //     return Response.success();
    // }

    @PostMapping(value = "/{id:-?\\d+}/accept/{providerId}/{rateId}")
    public Response<Void> accept(@PathVariable Long id, @PathVariable Long providerId, @PathVariable Long rateId) {
        quoteService.accept(id, providerId, rateId);
        return Response.success();
    }

    @PostMapping(value = "/{id:-?\\d+}/reject")
    public Response<Void> reject(@PathVariable Long id) {
        quoteService.reject(id);
        return Response.success();
    }

    // @PostMapping(value = "/{id:-?\\d+}/change-request")
    // public Response<Void> changeRequest(@PathVariable Long id, @Valid @RequestBody QuoteRejectDto changeRequestDto) {
    //     quoteService.changeRequest(id, changeRequestDto);
    //     return Response.success();
    // }

    @GetMapping(value = "/client/{id:-?\\d+}")
    public Response<List<QuoteResponseDto>> clientQuoteList(@PathVariable Long id) {
        return Response.success(quoteService.clientQuoteList(id));
    }
}
