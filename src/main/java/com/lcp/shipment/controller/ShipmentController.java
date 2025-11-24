package com.lcp.shipment.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.shipment.common.ApiShipmentMessage;
import com.lcp.shipment.dto.*;
import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/shipment")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping(value = "")
    public Response<ShipmentResponseDto> create(@Valid @RequestBody ShipmentCreateDto createDto) {
        return Response.success(shipmentService.create(createDto), ApiShipmentMessage.SHIPMENT_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<ShipmentResponseDto> update(@PathVariable Long id, @Valid @RequestBody ShipmentUpdateDto updateDto) {
        return Response.success(shipmentService.update(id, updateDto), ApiShipmentMessage.SHIPMENT_UPDATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}/update-status")
    public Response<ShipmentResponseDto> updateStatus(@PathVariable Long id, @Valid @RequestBody ShipmentStatusCreateDto updateDto) {
        return Response.success(shipmentService.updateStatus(id, updateDto), ApiShipmentMessage.SHIPMENT_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<ShipmentResponseDto> detail(@PathVariable Long id) {
        return Response.success(shipmentService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<ShipmentResponseDto>> list(ShipmentListRequest request) {
        return Response.success(shipmentService.list(request));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        shipmentService.delete(id);
        return Response.success(ApiShipmentMessage.SHIPMENT_DELETE_SUCCESS);
    }
}
