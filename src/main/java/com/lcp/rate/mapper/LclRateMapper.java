package com.lcp.rate.mapper;

import com.lcp.provider.mapper.ProviderMapper;
import com.lcp.rate.dto.LclRateResponseDto;
import com.lcp.rate.entity.LclRate;
import com.lcp.setting.mapper.CurrencyMapper;
import com.lcp.setting.mapper.UnlocoMapper;
import com.lcp.util.MapUtil;

public class LclRateMapper {
    public static LclRateResponseDto createResponse(LclRate entity) {
        LclRateResponseDto responseDto = new LclRateResponseDto();
        MapUtil.copyProperties(entity, responseDto);
//        if (entity.getKind() != null) {
//            responseDto.setKind(GoodKindMapper.createResponse(entity.getKind()));
//        }
        responseDto.setProvider(ProviderMapper.createResponse(entity.getProvider(), ProviderMapper.DetailIncludeFields));
        responseDto.setOrigin(UnlocoMapper.createResponse(entity.getOrigin()));
        responseDto.setDestination(UnlocoMapper.createResponse(entity.getDestination()));
        responseDto.setCurrency(CurrencyMapper.createResponse(entity.getCurrency()));

        return responseDto;
    }

}
