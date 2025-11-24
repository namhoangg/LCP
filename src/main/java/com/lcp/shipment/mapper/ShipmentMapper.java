package com.lcp.shipment.mapper;

import com.lcp.quote.mapper.QuoteMapper;
import com.lcp.rate.mapper.ContainerTypeMapper;
import com.lcp.shipment.dto.ShipmentCreateDto;
import com.lcp.shipment.dto.ShipmentResponseDto;
import com.lcp.shipment.entity.Shipment;
import com.lcp.util.MapUtil;

import java.util.stream.Collectors;

public class ShipmentMapper {
    public static Shipment createEntity(ShipmentCreateDto createDto) {
        Shipment shipment = new Shipment();
        MapUtil.copyProperties(createDto, shipment);
        return shipment;
    }

    public static ShipmentResponseDto createResponse(Shipment entity) {
        ShipmentResponseDto responseDto = new ShipmentResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getShipmentStatus() != null) {
            responseDto.setShipmentStatus(ShipmentStatusMapper.createResponse(entity.getShipmentStatus()));
        }

        if (entity.getQuote() != null) {
            responseDto.setQuote(QuoteMapper.createResponse(entity.getQuote(), QuoteMapper.DetailIncludeFields));
        }

        if (entity.getContainers() != null) {
            responseDto.setContainers(entity.getContainers().stream()
                    .map(ContainerMapper::createResponse)
                    .collect(Collectors.toList()));
        }
        return responseDto;
    }
}
