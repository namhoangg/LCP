package com.lcp.shipment.mapper;

import com.lcp.rate.mapper.ContainerTypeMapper;
import com.lcp.shipment.dto.ContainerResponseDto;
import com.lcp.shipment.dto.ContainerUpsertDto;
import com.lcp.shipment.entity.Container;
import com.lcp.util.MapUtil;

public class ContainerMapper {
    public static Container createEntity(ContainerUpsertDto createDto) {
        Container entity = new Container();
        MapUtil.copyProperties(createDto, entity);
        return entity;
    }

    public static ContainerResponseDto createResponse(Container entity) {
        ContainerResponseDto responseDto = new ContainerResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getContainerType() != null) {
            responseDto.setContainerType(ContainerTypeMapper.createResponse(entity.getContainerType()));
        }
        return responseDto;
    }
}
