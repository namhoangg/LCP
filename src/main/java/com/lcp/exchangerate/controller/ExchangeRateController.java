package com.lcp.exchangerate.controller;

import com.lcp.common.Response;
import com.lcp.exchangerate.dto.ExchangeRateResponseDto;
import com.lcp.exchangerate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping(value = "")
    public Response<ExchangeRateResponseDto> exchangeRate() {
        return Response.success(exchangeRateService.getExchangeRate(1L, 2L, LocalDate.now()));
    }
}
