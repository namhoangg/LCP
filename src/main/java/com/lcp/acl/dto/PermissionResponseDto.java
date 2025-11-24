package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PermissionResponseDto {


    private String id;
    private String name;
    private String description;
    private int scope;

    @JsonProperty("resource_actions")
    private List<ResourceActionDto> ResourceActions;

    private List<RoleResponseDto> roles;

    @JsonProperty("total_users")
    private long totalUsers;
}
