package com.lcp.acl.mapper;

import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.acl.dto.GetUsersByGroupResponseDto;
import com.lcp.acl.dto.RoleDetailResponseDto;
import com.lcp.acl.dto.RoleResponseDto;
import com.lcp.acl.entity.RoleEntity;
import com.lcp.staff.dto.StaffResponseDto;
import com.lcp.staff.mapper.StaffMapper;
import com.lcp.util.MapUtil;

import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleResponseDto createResponse(RoleEntity entity) {
        RoleResponseDto responseDto = new RoleResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        responseDto.setId(entity.getId().toString());
        if (entity.getType() == RoleTypeConstant.USER) {
            responseDto.setStaffs(entity.getStaffRoles().stream().map(staffRoleEntity -> StaffMapper.createResponse(staffRoleEntity.getStaff())).collect(Collectors.toList()));
            responseDto.setTotalStaffs((long) entity.getStaffRoles().size());
        } else if (entity.getType() == RoleTypeConstant.GROUP) {
            responseDto.setGroups(entity.getGroupRoles().stream().map(groupRoleEntity -> GroupMapper.createResponse(groupRoleEntity.getGroup())).collect(Collectors.toList()));
            long totalStaffs = entity.getGroupRoles().stream()
                    .flatMap(groupRoleEntity -> groupRoleEntity.getGroup().getStaffGroupStaffs().stream())
                    .map(staffGroupStaff -> staffGroupStaff.getStaff().getId())
                    .distinct()
                    .count();
            responseDto.setTotalStaffs(totalStaffs);
        }
        return responseDto;
    }

    public static RoleDetailResponseDto createDetailResponse(RoleEntity entity) {
        RoleDetailResponseDto responseDto = new RoleDetailResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        responseDto.setId(entity.getId().toString());
        if (entity.getType() == RoleTypeConstant.USER) {
            responseDto.setStaffs(entity.getStaffRoles().stream().map(staffRoleEntity -> StaffMapper.createResponse(staffRoleEntity.getStaff())).collect(Collectors.toList()));
            responseDto.setTotalStaffs((long) entity.getStaffRoles().size());
        } else if (entity.getType() == RoleTypeConstant.GROUP) {
            responseDto.setGroups(entity.getGroupRoles().stream().map(groupRoleEntity -> {
                GetUsersByGroupResponseDto groupResponseDto = new GetUsersByGroupResponseDto();
                groupResponseDto.setGroup(GroupMapper.createResponse(groupRoleEntity.getGroup()));
                groupResponseDto.setUsers(groupRoleEntity.getGroup().getStaffGroupStaffs().stream()
                        .map(staffGroupStaff -> StaffMapper.createResponse(staffGroupStaff.getStaff())).toArray(StaffResponseDto[]::new));
                return groupResponseDto;
            }).collect(Collectors.toList()));
            long totalStaffs = entity.getGroupRoles().stream()
                    .flatMap(groupRoleEntity -> groupRoleEntity.getGroup().getStaffGroupStaffs().stream())
                    .map(staffGroupStaff -> staffGroupStaff.getStaff().getId())
                    .distinct()
                    .count();
            responseDto.setTotalStaffs(totalStaffs);
        }
        return responseDto;
    }
}
