package com.lcp.rate.mapper;

import com.lcp.provider.mapper.ProviderMapper;
import com.lcp.rate.dto.FclRateCreateDto;
import com.lcp.rate.dto.FclRateDetailResponseDto;
import com.lcp.rate.dto.FclRateResponseDto;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRateDetail;
import com.lcp.rate.entity.FclRate_;
import com.lcp.setting.mapper.CurrencyMapper;
import com.lcp.setting.mapper.UnlocoMapper;
import com.lcp.util.MapUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class FclRateMapper {
    public static final List<String> DetailIncludeFields = List.of(
            FclRate_.PROVIDER,
            FclRate_.ORIGIN,
            FclRate_.DESTINATION,
            FclRate_.CURRENCY
    );

    public static FclRate createEntity(FclRateCreateDto createDto) {
        FclRate fclRate = new FclRate();
        MapUtil.copyProperties(createDto, fclRate);
        return fclRate;
    }

    public static FclRateResponseDto createResponse(FclRate entity, List<String> includeFields) {
        FclRateResponseDto responseDto = new FclRateResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (CollectionUtils.isNotEmpty(includeFields)) {
            if (includeFields.contains(FclRate_.PROVIDER) && entity.getProvider() != null) {
                responseDto.setProvider(ProviderMapper.createResponse(entity.getProvider(), ProviderMapper.DetailIncludeFields));
            }
            if (includeFields.contains(FclRate_.ORIGIN) && entity.getOrigin() != null) {
                responseDto.setOrigin(UnlocoMapper.createResponse(entity.getOrigin()));
            }
            if (includeFields.contains(FclRate_.DESTINATION) && entity.getDestination() != null) {
                responseDto.setDestination(UnlocoMapper.createResponse(entity.getDestination()));
            }
            if (includeFields.contains(FclRate_.CURRENCY) && entity.getCurrency() != null) {
                responseDto.setCurrency(CurrencyMapper.createResponse(entity.getCurrency()));
            }
        }

        if (entity.getDetails() != null) {
            responseDto.setDetails(
                entity.getDetails().stream()
                    .map(item -> {
                        FclRateDetailResponseDto detailResponseDto = new FclRateDetailResponseDto();
                        MapUtil.copyProperties(item, detailResponseDto);
                        detailResponseDto.setContainerTypeName(item.getContainerTypeName());
                        return detailResponseDto;
                    })
                    .collect(Collectors.toList())
            );
        }
        return responseDto;
    }

}
