package com.lcp.setting.mapper;

import com.lcp.setting.dto.CurrencyResponseDto;
import com.lcp.setting.entity.Currency;
import com.lcp.util.MapUtil;

public class CurrencyMapper {
    public static CurrencyResponseDto createResponse(Currency entity) {
        CurrencyResponseDto responseDto = new CurrencyResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
