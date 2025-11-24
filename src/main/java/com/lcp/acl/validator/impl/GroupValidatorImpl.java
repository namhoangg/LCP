package com.lcp.acl.validator.impl;

import com.lcp.acl.dto.AddUserToGroupRequestDto;
import com.lcp.acl.dto.CreateGroupRequestDto;
import com.lcp.acl.repository.StaffGroupRepository;
import com.lcp.acl.validator.GroupValidator;
import com.lcp.exception.ApiException;
import com.lcp.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupValidatorImpl implements GroupValidator {

    private final StaffGroupRepository staffGroupRepository;
    private final StaffRepository staffRepository;

    @Override
    public void validateCreateGroup(CreateGroupRequestDto request) {
        if (request.getName() == null) {
            throw new ApiException("Group name is required!");
        }
    }

    @Override
    public void validateAddUserToGroup(AddUserToGroupRequestDto request) {
        String groupId = request.getGroupId();
        String userId = request.getUserId();
        if (groupId == null || userId == null) {
            throw new ApiException("Group ID and User ID are required!");
        }

        if (!staffGroupRepository.existsById(UUID.fromString(groupId))) {
            throw new ApiException("Group does not exist!");
        }

        List<Long> userIds = Arrays.stream(userId.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        if (!staffRepository.existsAllByIdIn(userIds)) {
            throw new ApiException("Some users do not exist!");
        }
    }
}
