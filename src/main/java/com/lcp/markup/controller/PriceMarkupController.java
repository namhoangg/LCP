package com.lcp.markup.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.markup.common.ApiPriceMarkupMessage;
import com.lcp.markup.dto.PriceMarkupCreateDto;
import com.lcp.markup.dto.PriceMarkupListRequest;
import com.lcp.markup.dto.PriceMarkupResponseDto;
import com.lcp.markup.dto.PriceMarkupUpdateDto;
import com.lcp.markup.service.PriceMarkupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/price-markup")
@RequiredArgsConstructor
public class PriceMarkupController {
    private final PriceMarkupService priceMarkupService;

    @PostMapping(value = "")
    public Response<PriceMarkupResponseDto> create(@Valid @RequestBody PriceMarkupCreateDto createDto) {
        return Response.success(priceMarkupService.create(createDto), ApiPriceMarkupMessage.PRICE_MARKUP_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<PriceMarkupResponseDto> update(@PathVariable Long id, @Valid @RequestBody PriceMarkupUpdateDto updateDto) {
        return Response.success(priceMarkupService.update(id, updateDto), ApiPriceMarkupMessage.PRICE_MARKUP_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<PriceMarkupResponseDto> detail(@PathVariable Long id) {
        return Response.success(priceMarkupService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        priceMarkupService.delete(id);
        return Response.success(ApiPriceMarkupMessage.PRICE_MARKUP_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<PriceMarkupResponseDto>> list(PriceMarkupListRequest request) {
        return Response.success(priceMarkupService.list(request));
    }

}
