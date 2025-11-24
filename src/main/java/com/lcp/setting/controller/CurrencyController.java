package com.lcp.setting.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.setting.dto.CurrencyListRequest;
import com.lcp.setting.dto.CurrencyResponseDto;
import com.lcp.setting.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<CurrencyResponseDto> detail(@PathVariable Long id) {
        return Response.success(currencyService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<CurrencyResponseDto>> list(CurrencyListRequest request) {
        return Response.success(currencyService.list(request));
    }
}
