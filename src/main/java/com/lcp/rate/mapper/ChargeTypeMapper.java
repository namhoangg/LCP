package com.lcp.rate.mapper;

import com.lcp.rate.dto.ChargeTypeCreateDto;
import com.lcp.rate.dto.ChargeTypeResponseDto;
import com.lcp.rate.entity.ChargeType;
import com.lcp.util.MapUtil;

public class ChargeTypeMapper {
    public static ChargeType createEntity(ChargeTypeCreateDto createDto) {
        ChargeType chargeType = new ChargeType();
        MapUtil.copyProperties(createDto, chargeType);
        return chargeType;
    }

    public static ChargeTypeResponseDto createResponse(ChargeType entity) {
        ChargeTypeResponseDto responseDto = new ChargeTypeResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
