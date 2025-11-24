package com.lcp.acl.validator;

import com.lcp.acl.dto.AddUserToGroupRequestDto;
import com.lcp.acl.dto.CreateGroupRequestDto;

public interface GroupValidator {
    void validateCreateGroup(CreateGroupRequestDto request);

    void validateAddUserToGroup(AddUserToGroupRequestDto request);
}
