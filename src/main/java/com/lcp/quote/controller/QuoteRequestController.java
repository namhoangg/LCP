package com.lcp.quote.controller;

import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.quote.dto.LandingPageQuoteRequestDto;
import com.lcp.quote.dto.QuoteCreateRequestClient;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.dto.QuoteResponseDto;
import com.lcp.quote.service.QuoteRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/quote-request")
@RequiredArgsConstructor
public class QuoteRequestController {
    private final QuoteRequestService quoteRequestService;

    @PostMapping(value = "")
    public Response<Void> create(@Valid @RequestBody QuoteCreateRequestClient createDto) {
        quoteRequestService.create(createDto);
        return Response.success();
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<Void> update(@PathVariable Long id, @Valid @RequestBody QuoteCreateRequestClient updateDto) {
        quoteRequestService.update(id, updateDto);
        return Response.success();
    }

    // @GetMapping(value = "/{id:-?\\d+}")
    // public Response<QuoteRequestResponseDto> detail(@PathVariable Long id) {
    //     return Response.success(quoteRequestService.detail(id));
    // }

    @GetMapping(value = "")
    public Response<PageResponse<QuoteResponseDto>> list(QuoteListRequest request) {
        request.setIsRequest(true);
        return Response.success(quoteRequestService.list(request));
    }

    // @DeleteMapping(value = "/{id:-?\\d+}")
    // public Response<Void> delete(@PathVariable Long id) {
    //     quoteRequestService.delete(id);
    //     return Response.success(ApiQuoteMessage.QUOTE_DELETE_SUCCESS);
    // }

    @PostMapping(value = "/landing-page")
    public Response<EmptyResponse> requestQuote(@Valid @RequestBody LandingPageQuoteRequestDto request) {
        quoteRequestService.requestQuote(request);
        return Response.success();
    }
}
