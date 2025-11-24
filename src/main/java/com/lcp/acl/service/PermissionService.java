package com.lcp.acl.service;

import com.lcp.acl.dto.ListPermissionRequestDto;
import com.lcp.acl.dto.PermissionMatrixResponseDto;
import com.lcp.acl.dto.PermissionResponseDto;
import com.lcp.acl.dto.UpsertPermissionRequest;
import com.lcp.common.PageResponse;

public interface PermissionService {
    void upsertPermission(UpsertPermissionRequest request);

    PageResponse<PermissionResponseDto> listPermissions(ListPermissionRequestDto request);

    PermissionMatrixResponseDto getPermissionMatrix();

    PermissionResponseDto getPermission(String id);
}
