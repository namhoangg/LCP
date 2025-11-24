package com.lcp.shipment.service;

import com.lcp.common.PageResponse;
import com.lcp.shipment.dto.*;

public interface ShipmentService {
    ShipmentResponseDto create(ShipmentCreateDto createDto);
    ShipmentResponseDto update(Long id, ShipmentUpdateDto updateDto);
    ShipmentResponseDto updateStatus(Long id, ShipmentStatusCreateDto shipmentStatusCreateDto);
    ShipmentResponseDto detail(Long id);
    void delete(Long id);
    PageResponse<ShipmentResponseDto> list(ShipmentListRequest request);
}
