package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddUserToGroupRequestDto {

    @NotNull
    @NotEmpty
    @JsonProperty("group_id")
    private String groupId;

    @NotNull
    @NotEmpty
    @JsonProperty("user_ids")
    private String userId;
}
