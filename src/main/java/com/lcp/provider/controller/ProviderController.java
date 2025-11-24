package com.lcp.provider.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.provider.common.ApiProviderMessage;
import com.lcp.provider.dto.*;
import com.lcp.provider.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/provider")
@RequiredArgsConstructor
public class ProviderController {
    private final ProviderService providerService;

    @PostMapping(value = "")
    public Response<ProviderResponseDto> create(@Valid @RequestBody ProviderCreateDto createDto) {
        return Response.success(providerService.create(createDto), ApiProviderMessage.PROVIDER_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<ProviderResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProviderUpdateDto updateDto) {
        return Response.success(providerService.update(id, updateDto), ApiProviderMessage.PROVIDER_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<ProviderResponseDto> detail(@PathVariable Long id) {
        return Response.success(providerService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<ProviderResponseDto>> list(ProviderListRequest request) {
        return Response.success(providerService.list(request));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        providerService.delete(id);
        return Response.success(ApiProviderMessage.PROVIDER_DELETE_SUCCESS);
    }

    @PostMapping(value = "/quote/")
    public Response<List<ProviderByQuoteResponse>> getByQuote(@RequestBody GetByQuoteRequest request) {
        if (request.getOriginId() == 0 || request.getDestinationId() == 0 || request.getEtd() == null  || request.getContainerTypeIds() == null) {
            return Response.success(List.of());
        }
        return Response.success(providerService.getByQuote(request));
    }

}
