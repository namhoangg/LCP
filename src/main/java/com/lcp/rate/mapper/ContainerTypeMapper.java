package com.lcp.rate.mapper;

import com.lcp.rate.entity.ContainerType;
import com.lcp.rate.dto.ContainerTypeCreate;
import com.lcp.rate.dto.ContainerTypeResponse;
import com.lcp.util.MapUtil;

public class ContainerTypeMapper {
   public static ContainerType createEntity(ContainerTypeCreate createDto) {
        ContainerType containerType = new ContainerType();
        MapUtil.copyProperties(createDto, containerType);
        return containerType;
    }

    public static ContainerTypeResponse createResponse(ContainerType entity) {
        ContainerTypeResponse response = new ContainerTypeResponse();
        MapUtil.copyProperties(entity, response);
        return response;
    }
}
