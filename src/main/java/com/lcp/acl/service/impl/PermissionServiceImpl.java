package com.lcp.acl.service.impl;

import com.google.common.base.Strings;
import com.lcp.acl.dto.*;
import com.lcp.acl.entity.*;
import com.lcp.acl.mapper.PermissionMapper;
import com.lcp.acl.mapper.ResourceMapper;
import com.lcp.acl.repository.*;
import com.lcp.acl.service.PermissionService;
import com.lcp.acl.validator.PermissionValidator;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.security.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionValidator permissionValidator;
    private final PermissionRepository permissionRepository;
    private final PermissionRoleRepository permissionRoleRepository;
    private final PermissionResourceActionRepository permissionResourceActionRepository;
    private final PermissionCustomRepository permissionCustomRepository;
    private final ResourceActionRepository resourceActionRepository;
    private final ResourceRepository resourceRepository;
    private final ActionRepository actionRepository;

    @Transactional
    @Override
    public void upsertPermission(UpsertPermissionRequest request) {
        this.permissionValidator.validateUpsertPermission(request);

        Long currentUserId = UserDetailsCustom.getCurrentUserId();
        // Logic to upsert the permission in the database
        Permission permission;
        if (!Strings.isNullOrEmpty(request.getId())) {
            permission = permissionRepository.findById(UUID.fromString(request.getId()))
                    .orElseThrow(() -> new ApiException("Permission not found"));

            permission.setName(request.getName());
            permission.setDescription(request.getDescription());
            permission.setScope(request.getScope().ordinal());
            permission.setUpdatedBy(currentUserId);
            permission.setUpdatedAt(LocalDateTime.now());
        } else {
            permission = new Permission();
            permission.setId(UUID.randomUUID());
            permission.setName(request.getName());
            permission.setDescription(request.getDescription());
            permission.setScope(request.getScope().ordinal());
            permission.setCreatedAt(LocalDateTime.now());
            permission.setCreatedBy(currentUserId);
            permission.setUpdatedAt(LocalDateTime.now());
            permission.setUpdatedBy(currentUserId);
        }
        permissionRepository.save(permission);

        UUID permissionId = permission.getId();
        // Upsert Role Permission
        List<UUID> roleIds = request.getRoleIds()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        permissionRoleRepository.deleteAllByPermissionId(permissionId);
        List<PermissionRole> permissionRoles = roleIds
                .stream()
                .map(roleId -> {
                    PermissionRole permissionRole = new PermissionRole();
                    permissionRole.setPermissionId(permissionId);
                    permissionRole.setRoleId(roleId);
                    return permissionRole;
                })
                .collect(Collectors.toList());

        permissionRoleRepository.saveAll(permissionRoles);
        // Upsert permission resource action
        permissionResourceActionRepository.deleteAllByPermissionId(permissionId);
        List<PermissionResourceAction> permissionResourceActions = request.getResourceActionIds()
                .stream()
                .map(resourceActionId -> {
                    PermissionResourceAction permissionResourceAction = new PermissionResourceAction();
                    permissionResourceAction.setPermissionId(permissionId);
                    permissionResourceAction.setResourceActionId(resourceActionId);
                    return permissionResourceAction;
                })
                .collect(Collectors.toList());

        permissionResourceActionRepository.saveAll(permissionResourceActions);
    }

    @Override
    public PageResponse<PermissionResponseDto> listPermissions(ListPermissionRequestDto request) {
        Page<Permission> permissions = permissionCustomRepository.searchPermissions(request);

        return PageResponse.buildPageDtoResponse(permissions, PermissionMapper::createResponse);
    }

    @Override
    public PermissionMatrixResponseDto getPermissionMatrix() {
        List<ResourceAction> resourceActions = resourceActionRepository.findAll();
        List<Resource> resources = resourceRepository.findAll();
        List<Action> actions = actionRepository.findAll();

        Map<String, ResourceDto> resourceMap = resources
                .stream()
                .collect(Collectors.toMap(resource -> resource.getId().toString(), ResourceMapper::createResourceDto));

        Map<String, ActionDto> actionMap = actions
                .stream()
                .collect(Collectors.toMap(action -> action.getId().toString(), PermissionMapper::createActionDto));

        List<String> rootResourceIds = new ArrayList<>();

        for (Resource resource : resources) {
            if (resource.getParentId() == null) {
                rootResourceIds.add(resource.getId().toString());
            }
        }

        return PermissionMatrixResponseDto.newBuilder()
                .setActions(PermissionMapper.createActionDtos(actions))
                .setMatrix(getMatrixFromResourceIds(rootResourceIds, resourceMap, actionMap, resourceActions))
                .build();
    }

    @Override
    public PermissionResponseDto getPermission(String id) {
        Permission permission = permissionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ApiException("Permission not found"));

        return PermissionMapper.createResponse(permission);
    }

    private List<MatrixDto> getMatrixFromResourceIds(
            List<String> resourceIds,
            Map<String, ResourceDto> resourceById,
            Map<String, ActionDto> actionById,
            List<ResourceAction> resourceActions) {

        // For each child resource create a matrix element
        if (resourceIds == null || resourceIds.isEmpty()) {
            return null;
        }

        List<MatrixDto> matrix = new ArrayList<>();

        for (String resourceId : resourceIds) {
            Map<String, AdditionalDataDto> additionalData = new HashMap<>();
            List<ActionDto> actionByResourceIds = new ArrayList<>();

            for (ResourceAction resourceAction : resourceActions) {
                if (resourceAction.getResourceId().toString().equals(resourceId)) {
                    actionByResourceIds.add(actionById.get(resourceAction.getActionId().toString()));
                    additionalData.put(resourceAction.getActionId().toString(),
                            new AdditionalDataDto(
                                    resourceAction.getDescription(),
                                    String.valueOf(resourceAction.getId())
                            )
                    );
                }
            }

            List<String> descendantResourceIds = getChildResources(resourceId, resourceById);
            ResourceDto resource = resourceById.get(resourceId);

            MatrixDto matrixDto = new MatrixDto(
                    resource,
                    actionByResourceIds,
                    additionalData,
                    getMatrixFromResourceIds(descendantResourceIds, resourceById, actionById, resourceActions)
            );

            matrix.add(matrixDto);
        }

        return sortMatrixElement(matrix);
    }

    /**
     * Gets child resources for a given parent resource ID.
     */
    private List<String> getChildResources(String resourceId, Map<String, ResourceDto> resourceById) {
        List<String> childResourceIds = new ArrayList<>();

        for (Map.Entry<String, ResourceDto> entry : resourceById.entrySet()) {
            ResourceDto resource = entry.getValue();
            if (resource.getParent_id() != null && resource.getParent_id().equals(resourceId)) {
                childResourceIds.add(entry.getKey());
            }
        }

        return childResourceIds;
    }

    /**
     * Sorts matrix elements in ascending order by resource name.
     */
    private List<MatrixDto> sortMatrixElement(List<MatrixDto> matrix) {
        matrix.sort(new Comparator<MatrixDto>() {
            @Override
            public int compare(MatrixDto e1, MatrixDto e2) {
                return e1.getResources().getName().compareTo(e2.getResources().getName());
            }
        });

        return matrix;
    }
}
