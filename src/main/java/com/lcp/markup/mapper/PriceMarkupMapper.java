package com.lcp.markup.mapper;

import com.lcp.markup.dto.PriceMarkupCreateDto;
import com.lcp.markup.dto.PriceMarkupResponseDto;
import com.lcp.markup.entity.PriceMarkup;
import com.lcp.util.MapUtil;

import java.util.stream.Collectors;

public class PriceMarkupMapper {
    public static PriceMarkup createEntity(PriceMarkupCreateDto createDto) {
        PriceMarkup priceMarkup = new PriceMarkup();
        MapUtil.copyProperties(createDto, priceMarkup);
        return priceMarkup;
    }

    public static PriceMarkupResponseDto createResponse(PriceMarkup priceMarkup) {
        PriceMarkupResponseDto responseDto = new PriceMarkupResponseDto();
        MapUtil.copyProperties(priceMarkup, responseDto);
        if (priceMarkup.getPriceMarkupDetails() != null) {
            responseDto.setPriceMarkupDetails(
                priceMarkup.getPriceMarkupDetails().stream()
                    .map(PriceMarkupDetailMapper::createResponse)
                    .collect(Collectors.toList())
            );
        }
        return responseDto;
    }
}
