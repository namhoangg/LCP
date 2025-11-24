package com.lcp.acl.service;

import com.lcp.acl.dto.*;
import com.lcp.common.PageResponse;

import java.util.UUID;

public interface RoleService {
    PageResponse<RoleResponseDto> list(ListRoleRequestDto request);

    void createRole(CreateRoleRequestDto request);

    void delete(UUID id);

    RoleDetailResponseDto detail(UUID id);

    void addUsers(UUID id, AddRemoveUserRoleDto request);

    void removeUsers(UUID id, AddRemoveUserRoleDto request);
}
