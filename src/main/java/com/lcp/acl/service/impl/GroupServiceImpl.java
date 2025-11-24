package com.lcp.acl.service.impl;

import com.lcp.acl.dto.*;
import com.lcp.acl.entity.StaffGroupEntity;
import com.lcp.acl.entity.StaffGroupStaff;
import com.lcp.acl.mapper.GroupMapper;
import com.lcp.acl.repository.GroupRepositoryCustom;
import com.lcp.acl.repository.StaffGroupRepository;
import com.lcp.acl.repository.StaffGroupStaffRepository;
import com.lcp.acl.service.GroupService;
import com.lcp.acl.validator.GroupValidator;
import com.lcp.common.PageResponse;
import com.lcp.security.UserDetailsCustom;
import com.lcp.staff.dto.StaffResponseDto;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffCustomRepository;
import com.lcp.util.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupValidator groupValidator;
    private final StaffGroupRepository staffGroupRepository;
    private final GroupRepositoryCustom groupRepositoryCustom;
    private final StaffGroupStaffRepository staffGroupStaffRepository;
    private final StaffCustomRepository staffCustomRepository;

    @Override
    public PageResponse<GroupResponseDto> list(ListGroupRequestDto request) {
        Page<StaffGroupEntity> groups = groupRepositoryCustom.findAll(request);
        return PageResponse.buildPageDtoResponse(groups, GroupMapper::createResponse);
    }

    @Override
    public void createGroup(CreateGroupRequestDto request) {
        this.groupValidator.validateCreateGroup(request);

        // Create group
        StaffGroupEntity group = MapUtil.mapProperties(request, StaffGroupEntity.class);
        staffGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void addUserToGroup(AddUserToGroupRequestDto request) {
        this.groupValidator.validateAddUserToGroup(request);

        UUID groupId = UUID.fromString(request.getGroupId());

        Long[] userIds = Arrays.stream(request.getUserId().split(","))
                .map(Long::parseLong)
                .toArray(Long[]::new);
        Long currentUserId = UserDetailsCustom.getCurrentUserId();

        for (Long userId : userIds) {
            staffGroupStaffRepository.addUserToGroup(groupId, userId, currentUserId, LocalDateTime.now());
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        staffGroupRepository.deleteById(UUID.fromString(groupId));
    }

    @Override
    public void updateGroup(String groupId, CreateGroupRequestDto request) {
        this.groupValidator.validateCreateGroup(request);

        StaffGroupEntity group = staffGroupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        group.setName(request.getName());
        group.setDescription(request.getDescription());
        staffGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void removeUserFromGroup(RemoveUserFromGroupRequestDto request) {
        String groupId = request.getGroupId();
        String userId = request.getUserId();

        if (groupId == null || userId == null) {
            throw new IllegalArgumentException("Group ID and User ID are required!");
        }

        List<Long> userIds = Arrays.stream(userId.split(","))
                .map(Long::parseLong).collect(Collectors.toList());

        if (!staffGroupStaffRepository.existsAllByStaffIdInAndStaffGroupId(userIds, UUID.fromString(groupId))) {
            throw new IllegalArgumentException("Some users do not exist in the group!");
        }

        staffGroupStaffRepository.deleteAllByStaffGroupIdAndStaffIdIn(UUID.fromString(groupId), userIds);
    }

    @Override
    public GroupResponseDto getGroupById(String groupId) {
        StaffGroupEntity group = staffGroupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return GroupMapper.createResponse(group);
    }

    @Override
    public GetUsersByGroupResponseDto getUsersByGroup(String groupId) {
        StaffGroupEntity group = staffGroupRepository.findById(UUID.fromString(groupId))
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        List<StaffGroupStaff> users = staffGroupStaffRepository.findAllByStaffGroupId(group.getId());

        GetUsersByGroupResponseDto response = new GetUsersByGroupResponseDto();
        response.setGroup(GroupMapper.createResponse(group));
        response.setUsers(users.stream()
                .map(user -> MapUtil.mapProperties(user.getStaff(), StaffResponseDto.class))
                .toArray(StaffResponseDto[]::new));

        return response;
    }

    @Override
    public PageResponse<GetGroupByUserResponseDto> getGroupsByUser(ListGroupByUserDto request) {
        Page<Staff> users = staffCustomRepository.findAll(request);

        // Bad code, optimize it
        return PageResponse.buildPageDtoResponse(users, user -> {
            GetGroupByUserResponseDto response = new GetGroupByUserResponseDto();
            response.setUser(MapUtil.mapProperties(user, StaffResponseDto.class));
            List<StaffGroupEntity> groups = staffGroupStaffRepository.findAllByStaffId(user.getId())
                    .stream()
                    .map(StaffGroupStaff::getStaffGroup)
                    .collect(Collectors.toList());

            response.setGroups(groups.stream()
                    .map(GroupMapper::createResponse)
                    .collect(Collectors.toList()));

            return response;
        });
    }

}
