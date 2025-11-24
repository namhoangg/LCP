package com.lcp.rate.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.rate.common.ApiRateMessage;
import com.lcp.rate.dto.ChargeTypeCreateDto;
import com.lcp.rate.dto.ChargeTypeListRequest;
import com.lcp.rate.dto.ChargeTypeResponseDto;
import com.lcp.rate.dto.ChargeTypeUpdateDto;
import com.lcp.rate.service.ChargeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/charge-type")
@RequiredArgsConstructor
public class ChargeTypeController {
    private final ChargeTypeService chargeTypeService;

    @PostMapping(value = "")
    public Response<ChargeTypeResponseDto> create(@Valid @RequestBody ChargeTypeCreateDto createDto) {
        return Response.success(chargeTypeService.create(createDto), ApiRateMessage.CHARGE_TYPE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<ChargeTypeResponseDto> update(@PathVariable Long id, @Valid @RequestBody ChargeTypeUpdateDto updateDto) {
        return Response.success(chargeTypeService.update(id, updateDto), ApiRateMessage.CHARGE_TYPE_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<ChargeTypeResponseDto> detail(@PathVariable Long id) {
        return Response.success(chargeTypeService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        chargeTypeService.delete(id);
        return Response.success(ApiRateMessage.CHARGE_TYPE_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<ChargeTypeResponseDto>> list(ChargeTypeListRequest request) {
        return Response.success(chargeTypeService.list(request));
    }
}
