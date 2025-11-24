package com.lcp.shipment.mapper;

import com.lcp.shipment.dto.ShipmentStatusCreateDto;
import com.lcp.shipment.dto.ShipmentStatusResponseDto;
import com.lcp.shipment.entity.ShipmentStatus;
import com.lcp.util.MapUtil;

public class ShipmentStatusMapper {

     public static ShipmentStatus createEntity(ShipmentStatusCreateDto createDto) {
         ShipmentStatus shipmentStatus = new ShipmentStatus();
         MapUtil.copyProperties(createDto, shipmentStatus);
         return shipmentStatus;
     }

     public static ShipmentStatusResponseDto createResponse(ShipmentStatus entity) {
         ShipmentStatusResponseDto responseDto = new ShipmentStatusResponseDto();
         MapUtil.copyProperties(entity, responseDto);
         return responseDto;
     }
}
