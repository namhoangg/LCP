package com.lcp.setting.mapper;

import com.lcp.setting.dto.UnlocoResponseDto;
import com.lcp.setting.entity.Unloco;
import com.lcp.util.MapUtil;

public class UnlocoMapper {
    public static UnlocoResponseDto createResponse (Unloco entity) {
        UnlocoResponseDto responseDto = new UnlocoResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        responseDto.setDisplayName(entity.getDisplayName());
        return responseDto;
    }
}
