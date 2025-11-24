package com.lcp.rate.controller;

import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.rate.dto.*;
import com.lcp.rate.service.LclRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lcl-rate")
@RequiredArgsConstructor
public class LclRateController {
    private final LclRateService lclRateService;

    @PostMapping(value = "")
    public Response<EmptyResponse> create(@Valid @RequestBody LclRateCreateDto createDto) {
        lclRateService.create(createDto);
        return Response.success();
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<FclRateResponseDto> update(@Valid @RequestBody LclRateUpdateDto updateDto) {
        lclRateService.update(updateDto);
        return Response.success();
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<LclRateResponseDto> detail(@PathVariable Long id) {
        return Response.success(lclRateService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<LclRateResponseDto>> list(FclRateListRequest request) {
        return Response.success(lclRateService.list(request));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        lclRateService.delete(id);
        return Response.success();
    }
}
