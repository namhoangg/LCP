package com.lcp.acl.controller;

import com.lcp.acl.dto.*;
import com.lcp.acl.service.GroupService;
import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping(value = "")
    public Response<PageResponse<GroupResponseDto>> list(ListGroupRequestDto request) {
        return Response.success(groupService.list(request));
    }

    @PostMapping(value = "")
    public Response<EmptyResponse> create(@Valid @RequestBody CreateGroupRequestDto createGroupRequestDto) {
        groupService.createGroup(createGroupRequestDto);
        return Response.success();
    }

    @DeleteMapping(value = "users")
    public Response<EmptyResponse> removeUsers(@Valid @RequestBody RemoveUserFromGroupRequestDto request) {
        groupService.removeUserFromGroup(request);
        return Response.success();
    }

    @GetMapping(value = "/{groupId}")
    public Response<GroupResponseDto> getGroupById(@PathVariable String groupId) {
        GroupResponseDto groupResponseDto = groupService.getGroupById(groupId);
        return Response.success(groupResponseDto);
    }

    @GetMapping(value = "/users/groups")
    public Response<PageResponse<GetGroupByUserResponseDto>> getGroupsByUser(ListGroupByUserDto request) {
        return Response.success(groupService.getGroupsByUser(request));
    }

    @PutMapping(value = "/{groupId}")
    public Response<EmptyResponse> updateGroup(@PathVariable String groupId, @Valid @RequestBody CreateGroupRequestDto request) {
        groupService.updateGroup(groupId, request);
        return Response.success();
    }

    @DeleteMapping(value = "/{groupId}")
    public Response<EmptyResponse> deleteGroup(@PathVariable String groupId) {
        groupService.deleteGroup(groupId);
        return Response.success();
    }

    @GetMapping(value = "/{groupId}/users")
    public Response<GetUsersByGroupResponseDto> getUsersByGroup(@PathVariable String groupId) {
        GetUsersByGroupResponseDto usersByGroupResponseDto = groupService.getUsersByGroup(groupId);
        return Response.success(usersByGroupResponseDto);
    }

    @PostMapping(value = "/{groupId}/users")
    public Response<EmptyResponse> addUsers(@PathVariable String groupId, @Valid @RequestBody AddUserToGroupRequestDto request) {
        groupService.addUserToGroup(request);
        return Response.success();
    }
}
