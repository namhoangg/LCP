package com.lcp.setting.controller;

import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.setting.dto.ImportUnlocoDto;
import com.lcp.setting.dto.UnlocoListRequest;
import com.lcp.setting.dto.UnlocoRequestDto;
import com.lcp.setting.dto.UnlocoResponseDto;
import com.lcp.setting.service.UnlocoService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/unloco")
@RequiredArgsConstructor
public class UnlocoController {
    private final UnlocoService unlocoService;

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<UnlocoResponseDto> detail(@PathVariable Long id) {
        return Response.success(unlocoService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<UnlocoResponseDto>> list(UnlocoListRequest request) {
        return Response.success(unlocoService.list(request));
    }

    @PostMapping(value = "")
    public Response<UnlocoResponseDto> create(@RequestBody UnlocoRequestDto request) {
        return Response.success(unlocoService.create(request));
    }

    @PutMapping(value = "/{id:-?\\d+}")
    public Response<UnlocoResponseDto> update(@PathVariable Long id, @RequestBody UnlocoRequestDto request) {
        return Response.success(unlocoService.update(id, request));
    }

    @DeleteMapping(value = "/{id:-?\\d+}")
    public Response<Void> delete(@PathVariable Long id) {
        unlocoService.delete(id);
        return Response.success();
    }

    @PostMapping(value = "/export")
    public ResponseEntity<byte[]> export() {
        return unlocoService.export();
    }

    @PostMapping(value = "/import")
    public Response<Void> importUnlocos(@RequestBody List<ImportUnlocoDto> request) {
        unlocoService.importUnlocos(request);
        return Response.success();
    }

    @PostMapping(value = "/validate")
    public Response<List<ImportUnlocoDto>> validate(@RequestParam("file") MultipartFile file) {
        return Response.success(unlocoService.validate(file));
    }
}
