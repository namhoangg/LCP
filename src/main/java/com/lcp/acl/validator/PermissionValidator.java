package com.lcp.acl.validator;

import com.lcp.acl.dto.UpsertPermissionRequest;

public interface PermissionValidator {
    void validateUpsertPermission(UpsertPermissionRequest request);
}
