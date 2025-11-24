package com.lcp.rate.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.rate.common.ApiRateMessage;
import com.lcp.rate.dto.ServiceChargeCreateDto;
import com.lcp.rate.dto.ServiceChargeListRequest;
import com.lcp.rate.dto.ServiceChargeResponseDto;
import com.lcp.rate.dto.ServiceChargeUpdateDto;
import com.lcp.rate.service.ServiceChargeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/service-charge")
@RequiredArgsConstructor
public class ServiceChargeController {
    private final ServiceChargeService serviceChargeService;

    @PostMapping(value = "")
    public Response<ServiceChargeResponseDto> create(@Valid @RequestBody ServiceChargeCreateDto createDto) {
        return Response.success(serviceChargeService.create(createDto), ApiRateMessage.SERVICE_CHARGE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<ServiceChargeResponseDto> update(@PathVariable Long id, @Valid @RequestBody ServiceChargeUpdateDto updateDto) {
        return Response.success(serviceChargeService.update(id, updateDto), ApiRateMessage.SERVICE_CHARGE_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<ServiceChargeResponseDto> detail(@PathVariable Long id) {
        return Response.success(serviceChargeService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        serviceChargeService.delete(id);
        return Response.success(ApiRateMessage.SERVICE_CHARGE_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<ServiceChargeResponseDto>> list(ServiceChargeListRequest request) {
        return Response.success(serviceChargeService.list(request));
    }
}
