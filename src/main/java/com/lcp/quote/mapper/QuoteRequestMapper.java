package com.lcp.quote.mapper;

import com.lcp.client.mapper.ClientMapper;
import com.lcp.quote.dto.QuoteCreateRequest;
import com.lcp.quote.dto.QuoteCreateStaffDto;
import com.lcp.quote.dto.QuoteRequestCreateDto;
import com.lcp.quote.dto.QuoteRequestResponseDto;
import com.lcp.quote.entity.Quote;
import com.lcp.setting.mapper.UnlocoMapper;
import com.lcp.util.MapUtil;

public class QuoteRequestMapper {
    public static Quote createEntity(QuoteRequestCreateDto createDto) {
        Quote quote = new Quote();
        MapUtil.copyProperties(createDto, quote);
        return quote;
    }

    public static Quote createEntity(QuoteCreateStaffDto createDto) {
        Quote quote = new Quote();
        MapUtil.copyProperties(createDto, quote);
        return quote;
    }

    public static Quote createEntity(QuoteCreateRequest createDto) {
        Quote quote = new Quote();
        MapUtil.copyProperties(createDto, quote);
        return quote;
    }

    public static QuoteRequestResponseDto createResponse(Quote entity) {
        QuoteRequestResponseDto responseDto = new QuoteRequestResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getClient() != null) {
            responseDto.setClient(ClientMapper.createResponse(entity.getClient(), ClientMapper.DetailIncludeFields));
        }
        if (entity.getOrigin() != null) {
            responseDto.setOrigin(UnlocoMapper.createResponse(entity.getOrigin()));
        }
        if (entity.getDestination() != null) {
            responseDto.setDestination(UnlocoMapper.createResponse(entity.getDestination()));
        }
        return responseDto;
    }
}
