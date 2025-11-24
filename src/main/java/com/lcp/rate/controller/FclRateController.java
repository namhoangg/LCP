package com.lcp.rate.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.rate.common.ApiRateMessage;
import com.lcp.rate.dto.FclRateCreateDto;
import com.lcp.rate.dto.FclRateListRequest;
import com.lcp.rate.dto.FclRateResponseDto;
import com.lcp.rate.dto.FclRateUpdateDto;
import com.lcp.rate.service.FclRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/fcl-rate")
@RequiredArgsConstructor
public class FclRateController {
    private final FclRateService fclRateService;

    @PostMapping(value = "")
    public Response<FclRateResponseDto> create(@Valid @RequestBody FclRateCreateDto createDto) {
        return Response.success(fclRateService.create(createDto), ApiRateMessage.FCL_RATE_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<FclRateResponseDto> update(@PathVariable Long id, @Valid @RequestBody FclRateUpdateDto updateDto) {
        return Response.success(fclRateService.update(id, updateDto), ApiRateMessage.FCL_RATE_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<FclRateResponseDto> detail(@PathVariable Long id) {
        return Response.success(fclRateService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<FclRateResponseDto>> list(FclRateListRequest request) {
        return Response.success(fclRateService.list(request));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        fclRateService.delete(id);
        return Response.success(ApiRateMessage.FCL_RATE_DELETE_SUCCESS);
    }

    @GetMapping(value = "/provider/{providerId}")
    public Response<FclRateResponseDto> getByProvider(@PathVariable Long providerId) {
        return Response.success(fclRateService.getByProvider(providerId));
    }
}
