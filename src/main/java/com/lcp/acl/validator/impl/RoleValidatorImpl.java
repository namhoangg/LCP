package com.lcp.acl.validator.impl;

import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.acl.dto.CreateRoleRequestDto;
import com.lcp.acl.repository.StaffGroupRepository;
import com.lcp.acl.validator.RoleValidator;
import com.lcp.exception.ApiException;
import com.lcp.staff.repository.StaffRepository;
import com.lcp.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Log4j2
public class RoleValidatorImpl implements RoleValidator {

    private final StaffRepository staffRepository;
    private final StaffGroupRepository staffGroupRepository;

    @Override
    public void validateCreateRole(CreateRoleRequestDto request) {
        if (request.getName().isEmpty()) {
            throw new ApiException("Name is required!");
        }

        if (request.getType() != RoleTypeConstant.USER && request.getType() != RoleTypeConstant.GROUP) {
            throw new ApiException("Role type is invalid!");
        }

        if (request.getActors().isEmpty()) {
            throw new ApiException("Actors are required!");
        }

        switch (request.getType()) {
            case USER:
                List<Long> userIds = StringUtil.stringToLongList(request.getActors());

                if (userIds.isEmpty()) {
                    throw new ApiException("Actors are required!");
                }

                if (!staffRepository.existsAllByIdIn(userIds)) {
                    throw new ApiException("User not found!");
                }
                break;
            case GROUP:
                List<UUID> groupIds = StringUtil.stringToUUIDList(request.getActors());
                if (groupIds.isEmpty()) {
                    throw new ApiException("Actors are required!");
                }
                if (!staffGroupRepository.existsAllByIdIn(groupIds)) {
                    throw new ApiException("Group not found!");
                }
                break;
            default:
                throw new ApiException("Role type is invalid!");
        }
    }
}
