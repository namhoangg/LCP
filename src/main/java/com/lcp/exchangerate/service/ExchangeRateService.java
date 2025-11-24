package com.lcp.exchangerate.service;

import com.lcp.exchangerate.dto.ExchangeRateResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeRateService {
    ExchangeRateResponseDto getExchangeRate(Long fromCurrencyId, Long toCurrencyId, LocalDate createdDate);
}
