package com.lcp.acl.validator;

import com.lcp.acl.dto.CreateRoleRequestDto;

public interface RoleValidator {
    void validateCreateRole(CreateRoleRequestDto request);
}
