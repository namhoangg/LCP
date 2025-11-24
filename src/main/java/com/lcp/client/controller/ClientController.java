package com.lcp.client.controller;

import com.lcp.client.common.ApiClientMessage;
import com.lcp.client.dto.ClientCreateDto;
import com.lcp.client.dto.ClientListRequest;
import com.lcp.client.dto.ClientResponseDto;
import com.lcp.client.dto.ClientUpdateDto;
import com.lcp.client.service.ClientService;
import com.lcp.common.Constant;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping(value = "", consumes = Constant.API_CONTENT_TYPE)
    public Response<ClientResponseDto> create (@RequestBody ClientCreateDto createDto) {
        return Response.success(clientService.create(createDto), ApiClientMessage.CLIENT_CREATE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}", consumes = Constant.API_CONTENT_TYPE)
    public Response<ClientResponseDto> update(@PathVariable Long id, @RequestBody ClientUpdateDto updateDto) {
        return Response.success(clientService.update(id, updateDto), ApiClientMessage.CLIENT_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<ClientResponseDto> detail(@PathVariable Long id) {
        return Response.success(clientService.detail(id));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return Response.success(ApiClientMessage.CLIENT_DELETE_SUCCESS);
    }

    @GetMapping(value = "")
    public Response<PageResponse<ClientResponseDto>> list(ClientListRequest request) {
        return Response.success(clientService.list(request));
    }
}
