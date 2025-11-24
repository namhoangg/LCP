package com.lcp.rate.mapper;

import com.lcp.provider.mapper.ProviderMapper;
import com.lcp.rate.dto.ServiceChargeCreateDto;
import com.lcp.rate.dto.ServiceChargeResponseDto;
import com.lcp.rate.entity.ServiceCharge;
import com.lcp.setting.mapper.CurrencyMapper;
import com.lcp.util.MapUtil;

import java.util.List;

public class ServiceChargeMapper {
    public static ServiceCharge createEntity(ServiceChargeCreateDto createDto) {
        ServiceCharge serviceType = new ServiceCharge();
        MapUtil.copyProperties(createDto, serviceType);
        return serviceType;
    }

    public static ServiceChargeResponseDto createResponse(ServiceCharge entity) {
        ServiceChargeResponseDto responseDto = new ServiceChargeResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getChargeType() != null) {
            responseDto.setChargeType(ChargeTypeMapper.createResponse(entity.getChargeType()));
        }
        if (entity.getCurrency() != null) {
            responseDto.setCurrency(CurrencyMapper.createResponse(entity.getCurrency()));
        }
        if (entity.getProvider() != null) {
            responseDto.setProvider(ProviderMapper.createResponse(entity.getProvider(), List.of("companyInfo", "contactPersonInfo")));
        }
        return responseDto;
    }
}
