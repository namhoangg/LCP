package com.lcp.acl.controller;

import com.lcp.acl.dto.*;
import com.lcp.acl.service.RoleService;
import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping(value = "")
    public Response<EmptyResponse> create(@Valid @RequestBody CreateRoleRequestDto createRoleRequestDto) {
        roleService.createRole(createRoleRequestDto);
        return Response.success();
    }

    @GetMapping(value = "/{id}")
    public Response<RoleDetailResponseDto> detail(@PathVariable UUID id) {
        return Response.success(roleService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<RoleResponseDto>> list(ListRoleRequestDto request) {
        return Response.success(roleService.list(request));
    }

    @DeleteMapping("/{id}")
    public Response<Void> delete(@PathVariable UUID id) {
        roleService.delete(id);
        return Response.success();
    }

    @PostMapping(value = "/{id}/users/add")
    public Response<EmptyResponse> addUsers(@PathVariable UUID id, @RequestBody AddRemoveUserRoleDto request) {
        roleService.addUsers(id, request);
        return Response.success();
    }

    @PostMapping(value = "/{id}/users/remove")
    public Response<EmptyResponse> removeUsers(@PathVariable UUID id, @RequestBody AddRemoveUserRoleDto request) {
        roleService.removeUsers(id, request);
        return Response.success();
    }

}
