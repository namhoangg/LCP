package com.lcp.acl.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RemoveUserFromGroupRequestDto {

    @NotNull
    @NotEmpty
    private String groupId;

    @NotEmpty
    @NotNull
    private String userId;
}
