package com.lcp.acl.controller;

import com.lcp.acl.dto.ListPermissionRequestDto;
import com.lcp.acl.dto.PermissionMatrixResponseDto;
import com.lcp.acl.dto.PermissionResponseDto;
import com.lcp.acl.dto.UpsertPermissionRequest;
import com.lcp.acl.service.PermissionService;
import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping(value = "")
    public Response<PageResponse<PermissionResponseDto>> listPermissions(ListPermissionRequestDto request) {
        return Response.success(permissionService.listPermissions(request));
    }

    @PostMapping(value = "")
    public Response<EmptyResponse> upsertPermissions(@Valid @RequestBody UpsertPermissionRequest request) {
        permissionService.upsertPermission(request);
        return Response.success();
    }

    @GetMapping(value = "/matrix")
    public Response<PermissionMatrixResponseDto> getPermissionMatrix() {
        return Response.success(permissionService.getPermissionMatrix());
    }

    @GetMapping(value = "/{id}")
    public Response<PermissionResponseDto> getPermission(@PathVariable String id) {
        return Response.success(permissionService.getPermission(id));
    }
}
