package com.lcp.acl.service;

import com.lcp.acl.dto.*;
import com.lcp.common.PageResponse;

public interface GroupService {
    PageResponse<GroupResponseDto> list(ListGroupRequestDto request);

    void createGroup(CreateGroupRequestDto request);

    void addUserToGroup(AddUserToGroupRequestDto request);

    void deleteGroup(String groupId);

    void updateGroup(String groupId, CreateGroupRequestDto request);

    void removeUserFromGroup(RemoveUserFromGroupRequestDto request);

    GroupResponseDto getGroupById(String groupId);

    GetUsersByGroupResponseDto getUsersByGroup(String groupId);

    PageResponse<GetGroupByUserResponseDto> getGroupsByUser(ListGroupByUserDto request);
}
