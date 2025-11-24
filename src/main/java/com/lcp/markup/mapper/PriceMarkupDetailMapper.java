package com.lcp.markup.mapper;

import com.lcp.markup.dto.PriceMarkupDetailCreateDto;
import com.lcp.markup.dto.PriceMarkupDetailResponseDto;
import com.lcp.markup.dto.PriceMarkupResponseDto;
import com.lcp.markup.entity.PriceMarkupDetail;
import com.lcp.util.MapUtil;

public class PriceMarkupDetailMapper {
    public static PriceMarkupDetail createEntity(PriceMarkupDetailCreateDto createDto) {
        PriceMarkupDetail priceMarkupDetail = new PriceMarkupDetail();
        MapUtil.copyProperties(createDto, priceMarkupDetail);
        return priceMarkupDetail;
    }

    public static PriceMarkupDetailResponseDto createResponse(PriceMarkupDetail priceMarkupDetail) {
        PriceMarkupDetailResponseDto responseDto = new PriceMarkupDetailResponseDto();
        MapUtil.copyProperties(priceMarkupDetail, responseDto);
        return responseDto;
    }
}
