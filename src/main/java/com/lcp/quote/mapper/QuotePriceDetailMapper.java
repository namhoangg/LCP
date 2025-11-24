package com.lcp.quote.mapper;

import com.lcp.quote.dto.QuotePriceDetailCreateDto;
import com.lcp.quote.dto.QuotePriceDetailResponseDto;
import com.lcp.quote.entity.QuotePriceDetail;
import com.lcp.rate.mapper.ChargeTypeMapper;
import com.lcp.security.UserDetailsCustom;
import com.lcp.setting.mapper.CurrencyMapper;
import com.lcp.util.MapUtil;

import java.util.Objects;

public class QuotePriceDetailMapper {

    public static QuotePriceDetail createEntity(QuotePriceDetailCreateDto createDto) {
        QuotePriceDetail quotePriceDetail = new QuotePriceDetail();
        MapUtil.copyProperties(createDto, quotePriceDetail);
        return quotePriceDetail;
    }

    public static QuotePriceDetailResponseDto createResponse(QuotePriceDetail entity) {
        QuotePriceDetailResponseDto responseDto = new QuotePriceDetailResponseDto();
        MapUtil.copyProperties(entity, responseDto);

        if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
            responseDto.setBasePrice(null);
        }

        if (entity.getChargeType() != null) {
            responseDto.setChargeType(ChargeTypeMapper.createResponse(entity.getChargeType()));
        }

        if (entity.getCurrency() != null) {
            responseDto.setCurrency(CurrencyMapper.createResponse(entity.getCurrency()));
        }
        return responseDto;
    }
}
