package com.lcp.acl.validator.impl;

import com.google.common.base.Strings;
import com.lcp.acl.constants.enums.Scope;
import com.lcp.acl.dto.UpsertPermissionRequest;
import com.lcp.acl.repository.PermissionRepository;
import com.lcp.acl.repository.ResourceActionRepository;
import com.lcp.acl.repository.RoleRepository;
import com.lcp.acl.validator.PermissionValidator;
import com.lcp.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionValidatorImpl implements PermissionValidator {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final ResourceActionRepository resourceActionRepository;

    @Override
    public void validateUpsertPermission(UpsertPermissionRequest request) {
        if (!Strings.isNullOrEmpty(request.getId()) && !permissionRepository.existsById(UUID.fromString(request.getId()))) {
            throw new ApiException("Permission with ID " + request.getId() + " does not exist.");
        }

        if (Strings.isNullOrEmpty(request.getName())) {
            throw new ApiException("Permission name is required.");
        }

        UUID permissionId = Strings.isNullOrEmpty(request.getId()) ? null : UUID.fromString(request.getId());

        // Check for duplicate permission name
        boolean nameExists;
        if (permissionId == null) {
            nameExists = permissionRepository.existsByName(request.getName());
        } else {
            nameExists = permissionRepository.existsByNameAndIdNot(request.getName(), permissionId);
        }

        if (nameExists) {
            throw new ApiException("Permission name is duplicated");
        }

        // Validate scope
        if (request.getScope() == null || request.getScope() == Scope.UNSPECIFIED) {
            throw new ApiException("Unsupported scope");
        }

        // Validate roles IDs
        List<UUID> roleIds = request.getRoleIds()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        boolean allRoleExist = roleRepository.existsAllByIdIn(roleIds);
        if (!allRoleExist) {
            throw new ApiException("Invalid condition IDs");
        }

        // Validate resource action IDs
        List<Integer> resourceActionIds = request.getResourceActionIds();

        boolean allResourceActionsExist = resourceActionRepository.existsAllByIdIn(resourceActionIds);
        if (!allResourceActionsExist) {
            throw new ApiException("Invalid resource action IDs");
        }
    }
}
