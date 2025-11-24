package com.lcp.acl.mapper;

import com.lcp.acl.dto.ActionDto;
import com.lcp.acl.dto.PermissionResponseDto;
import com.lcp.acl.dto.ResourceActionDto;
import com.lcp.acl.dto.ResourceDto;
import com.lcp.acl.entity.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionMapper {
    public static PermissionResponseDto createResponse(Permission entity) {
        PermissionResponseDto responseDto = new PermissionResponseDto();
        responseDto.setId(entity.getId().toString());
        responseDto.setName(entity.getName());
        responseDto.setDescription(entity.getDescription());
        responseDto.setScope(entity.getScope());

        List<RoleEntity> roles = entity.getPermissionRole().stream()
                .map(PermissionRole::getRole)
                .collect(Collectors.toList());


        List<GroupRoleEntity> groupRoles = roles.stream()
                .flatMap(role -> role.getGroupRoles().stream())
                .collect(Collectors.toList());

        List<StaffRoleEntity> staffRoles = roles.stream()
                .flatMap(role -> role.getStaffRoles().stream())
                .collect(Collectors.toList());

        List<StaffGroupStaff> staffGroupStaffs = groupRoles.stream()
                .flatMap(groupRole -> groupRole.getGroup().getStaffGroupStaffs().stream())
                .collect(Collectors.toList());

        // Count total base on staff Roles and staffGroupStaff if staffId is not in the map
        Map<Long, Boolean> seen = new HashMap<>();
        for (StaffRoleEntity staffRole : staffRoles) {
            seen.put(staffRole.getStaff().getId(), true);
        }

        for (StaffGroupStaff staffGroupStaff : staffGroupStaffs) {
            if (!seen.containsKey(staffGroupStaff.getStaff().getId())) {
                seen.put(staffGroupStaff.getStaff().getId(), true);
            }
        }
        responseDto.setTotalUsers(seen.size());
        responseDto.setRoles(roles.stream().map(RoleMapper::createResponse).collect(Collectors.toList()));

        List<PermissionResourceAction> permissionResourceActions = entity.getPermissionResourceAction();

        List<ResourceActionDto> resourceActions = permissionResourceActions.stream()
                .map(permissionResourceAction -> {
                    ResourceAction resourceAction = permissionResourceAction.getResourceAction();
                    ResourceActionDto resourceActionDto = new ResourceActionDto();
                    resourceActionDto.setId(resourceAction.getId());
                    resourceActionDto.setDescription(resourceAction.getDescription());
                    ResourceDto resourceDto = ResourceMapper.createResourceDto(resourceAction.getResource());
                    resourceActionDto.setResource(resourceDto);
                    ActionDto actionDto = new ActionDto();
                    actionDto.setId(resourceAction.getAction().getId().toString());
                    actionDto.setName(resourceAction.getAction().getName());
                    resourceActionDto.setAction(actionDto);

                    return resourceActionDto;
                })
                .collect(Collectors.toList());

        responseDto.setResourceActions(resourceActions);


        return responseDto;
    }

    public static ActionDto createActionDto(Action action) {
        ActionDto actionDto = new ActionDto();
        actionDto.setId(action.getId().toString());
        actionDto.setName(action.getName());
        return actionDto;
    }

    public static List<ActionDto> createActionDtos(List<Action> actions) {
        return actions.stream()
                .map(PermissionMapper::createActionDto)
                .collect(Collectors.toList());
    }
}
