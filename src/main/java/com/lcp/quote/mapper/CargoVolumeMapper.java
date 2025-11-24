package com.lcp.quote.mapper;

import com.lcp.quote.dto.CargoVolumeCreateDto;
import com.lcp.quote.dto.CargoVolumeResponseDto;
import com.lcp.quote.entity.CargoVolume;
import com.lcp.rate.mapper.FclRateMapper;
import com.lcp.rate.mapper.LclRateMapper;
import com.lcp.util.MapUtil;
import com.lcp.security.UserDetailsCustom;
import java.util.Objects;

public class CargoVolumeMapper {
    public static CargoVolume createEntity(CargoVolumeCreateDto createDto) {
        CargoVolume cargoVolume = new CargoVolume();
        MapUtil.copyProperties(createDto, cargoVolume);
        return cargoVolume;
    }

    public static CargoVolumeResponseDto createResponse(CargoVolume entity) {
        CargoVolumeResponseDto responseDto = new CargoVolumeResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getFclRate() != null) {
            responseDto.setFclRateId(entity.getFclRate().getId());
            responseDto.setFclRate(FclRateMapper.createResponse(entity.getFclRate(), FclRateMapper.DetailIncludeFields));
        }
        if (entity.getLclRate() != null) {
            responseDto.setLclRateId(entity.getLclRate().getId());
            responseDto.setLclRate(LclRateMapper.createResponse(entity.getLclRate()));
        }
        return responseDto;
    }
}
