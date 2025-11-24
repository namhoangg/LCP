package com.lcp.shipment.controller;

import com.lcp.common.Constant;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.shipment.common.ApiShipmentMessage;
import com.lcp.shipment.dto.ContainerIdsRequestDto;
import com.lcp.shipment.dto.ContainerListRequest;
import com.lcp.shipment.dto.ContainerResponseDto;
import com.lcp.shipment.dto.ContainerWrapperUpsertDto;
import com.lcp.shipment.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipment/{shipmentId:-?\\d+}/container")
public class ContainerController {
    private final ContainerService containerService;

    @PostMapping(value = "", consumes = Constant.API_CONTENT_TYPE)
    public Response<Void> upsert(
        @Valid @RequestBody ContainerWrapperUpsertDto upsertDto,
        @PathVariable Long shipmentId
    ) {
        upsertDto.setShipmentId(shipmentId);
        containerService.upsert(upsertDto);
        return Response.success(ApiShipmentMessage.CONTAINER_UPSERT_SUCCESS);
    }

    @GetMapping(value = "", produces = Constant.API_CONTENT_TYPE)
    public Response<PageResponse<ContainerResponseDto>> list(
            @ModelAttribute ContainerListRequest request,
            @PathVariable Long shipmentId
    ) {
        request.setShipmentId(shipmentId);
        return Response.success(containerService.list(request));
    }

    @DeleteMapping(value = "/bulk-delete")
    public Response<Void> bulkDelete(
            @Valid @ModelAttribute ContainerIdsRequestDto deleteDto,
            @PathVariable Long shipmentId
    ) {
        deleteDto.setShipmentId(shipmentId);
        containerService.bulkDelete(deleteDto);
        return Response.success(ApiShipmentMessage.CONTAINER_DELETE_SUCCESS);
    }
}
