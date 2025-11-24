package com.lcp.acl.service.impl;

import com.google.common.base.Strings;
import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.acl.dto.*;
import com.lcp.acl.entity.GroupRoleEntity;
import com.lcp.acl.entity.RoleEntity;
import com.lcp.acl.entity.StaffRoleEntity;
import com.lcp.acl.mapper.RoleMapper;
import com.lcp.acl.repository.GroupRoleRepository;
import com.lcp.acl.repository.RoleRepository;
import com.lcp.acl.repository.RoleRepositoryCustom;
import com.lcp.acl.repository.StaffRoleRepository;
import com.lcp.acl.service.RoleService;
import com.lcp.acl.validator.RoleValidator;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.util.MapUtil;
import com.lcp.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleValidator roleValidator;
    private final RoleRepository roleRepository;
    private final StaffRoleRepository staffRoleRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final RoleRepositoryCustom roleRepositoryCustom;

    @Override
    public PageResponse<RoleResponseDto> list(ListRoleRequestDto request) {
        Page<RoleEntity> roleEntities = roleRepositoryCustom.findAll(request);
        return PageResponse.buildPageDtoResponse(roleEntities, RoleMapper::createResponse);
    }

    @Override
    @Transactional
    public void createRole(CreateRoleRequestDto request) {
        this.roleValidator.validateCreateRole(request);
        UUID roleId;
        if (!Strings.isNullOrEmpty(request.getId())) {
            roleId = UUID.fromString(request.getId());
            // Update role
            RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(() -> new ApiException("Role not found!"));
            roleEntity.setName(request.getName());
            roleEntity.setDescription(request.getDescription());
            roleRepository.save(roleEntity);
        } else {
            // Create role
            RoleEntity roleEntity = new RoleEntity();
            MapUtil.copyProperties(request, roleEntity);
            roleRepository.save(roleEntity);
            roleId = roleEntity.getId();
        }

        // Mapping actors
        switch (request.getType()) {
            case USER:
                upsertRoleStaff(roleId, StringUtil.stringToLongList(request.getActors()));
                break;
            case GROUP:
                upsertRoleGroup(roleId, request);
                break;
            default:
                throw new RuntimeException("Role type is invalid!");
        }
    }

    private void upsertRoleStaff(UUID roleId, List<Long> staffIds) {
        // Remove staff roles
        staffRoleRepository.deleteAllByRoleId(roleId);

        // Add new staff roles
        List<StaffRoleEntity> newStaffRoles = new ArrayList<>();
        for (Long staffId : staffIds) {
            StaffRoleEntity staffRoleEntity = new StaffRoleEntity();
            staffRoleEntity.setRoleId(roleId);
            staffRoleEntity.setStaffId(staffId);
            newStaffRoles.add(staffRoleEntity);
        }

        staffRoleRepository.saveAll(newStaffRoles);
    }

    private void upsertRoleGroup(UUID roleId, CreateRoleRequestDto request) {
        // Remove group roles
        groupRoleRepository.deleteAllByRoleId(roleId);

        List<UUID> groupIds = StringUtil.stringToUUIDList(request.getActors());

        // Add new group roles
        List<GroupRoleEntity> newGroupRoles = new ArrayList<>();
        for (UUID groupId : groupIds) {
            GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
            groupRoleEntity.setRoleId(roleId);
            groupRoleEntity.setGroupId(groupId);
            newGroupRoles.add(groupRoleEntity);
        }


        groupRoleRepository.saveAll(newGroupRoles);
    }

    public void delete(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDetailResponseDto detail(UUID id) {
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new ApiException("Role not found!"));
        return RoleMapper.createDetailResponse(roleEntity);
    }

    @Override
    public void addUsers(UUID id, AddRemoveUserRoleDto request) {
        String userIds = request.getUserIds();
        if (Strings.isNullOrEmpty(userIds)) {
            throw new ApiException("actors are required!");
        }

        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new ApiException("Role not found!"));

        if (roleEntity.getType() == RoleTypeConstant.USER) {
            List<Long> staffIds = StringUtil.stringToLongList(userIds);

            List<StaffRoleEntity> staffRoleEntities = staffRoleRepository.findAllByRoleId(id);

            Map<Long, Boolean> exists = staffRoleEntities.stream()
                    .collect(Collectors.toMap(StaffRoleEntity::getStaffId, staffRole -> true));

            List<StaffRoleEntity> newStaffRoles = new ArrayList<>();
            for (Long staffId : staffIds) {
                if (!exists.containsKey(staffId)) {
                    StaffRoleEntity staffRoleEntity = new StaffRoleEntity();
                    staffRoleEntity.setRoleId(id);
                    staffRoleEntity.setStaffId(staffId);
                    newStaffRoles.add(staffRoleEntity);
                }
            }
            staffRoleRepository.saveAll(newStaffRoles);
        } else {
            List<UUID> groupIds = StringUtil.stringToUUIDList(userIds);
            List<GroupRoleEntity> groupRoleEntities = groupRoleRepository.findAllByRoleId(id);
            Map<UUID, Boolean> exists = groupRoleEntities.stream()
                    .collect(Collectors.toMap(GroupRoleEntity::getGroupId, groupRole -> true));
            List<GroupRoleEntity> newGroupRoles = new ArrayList<>();
            for (UUID groupId : groupIds) {
                if (!exists.containsKey(groupId)) {
                    GroupRoleEntity groupRoleEntity = new GroupRoleEntity();
                    groupRoleEntity.setRoleId(id);
                    groupRoleEntity.setGroupId(groupId);
                    newGroupRoles.add(groupRoleEntity);
                }
            }
            groupRoleRepository.saveAll(newGroupRoles);
        }

        //Todo: refactor
        // Now we just query it first to know if the user exists, if not exists then add it
        // If exists then do nothing
        // In the future we can use a batch insert to improve performance
    }

    @Override
    @Transactional
    public void removeUsers(UUID id, AddRemoveUserRoleDto request) {
        String userIds = request.getUserIds();
        if (Strings.isNullOrEmpty(userIds)) {
            throw new ApiException("actors are required!");
        }

        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new ApiException("Role not found!"));
        if (roleEntity.getType() == RoleTypeConstant.USER) {
            List<Long> staffIds = StringUtil.stringToLongList(userIds);

            staffRoleRepository.deleteAllByRoleIdAndStaffIdIn(id, staffIds);
        } else {
            List<UUID> groupIds = StringUtil.stringToUUIDList(userIds);
            groupRoleRepository.deleteAllByRoleIdAndGroupIdIn(id, groupIds);
        }
    }
}
