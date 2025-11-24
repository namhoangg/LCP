package com.lcp.setting.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.setting.dto.CurrencyListRequest;
import com.lcp.setting.dto.CurrencyResponseDto;
import com.lcp.setting.entity.Currency;
import com.lcp.setting.mapper.CurrencyMapper;
import com.lcp.setting.repository.CurrencyRepository;
import com.lcp.setting.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Override
    public PageResponse<CurrencyResponseDto> list(CurrencyListRequest request) {
        Page<Currency> currencies = currencyRepository.list(request);
        return PageResponse.buildPageDtoResponse(currencies,
                CurrencyMapper::createResponse);
    }

    @Override
    public CurrencyResponseDto detail(Long id) {
        Currency currency = get(id);
        return CurrencyMapper.createResponse(currency);
    }

    private Currency get(Long id) {
        Optional<Currency> currencyOptional = currencyRepository.findById(id);
        if (currencyOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Currency not found"));
        }
        return currencyOptional.get();
    }
}
