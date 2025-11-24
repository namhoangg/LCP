package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcp.acl.constants.enums.Scope;
import lombok.Data;

import java.util.List;

@Data
public class UpsertPermissionRequest {
    private String id;
    private String name;
    private String description;
    private Scope scope;

    @JsonProperty("resource_action_ids")
    private List<Integer> resourceActionIds;

    @JsonProperty("role_ids")
    private List<String> roleIds;
}
