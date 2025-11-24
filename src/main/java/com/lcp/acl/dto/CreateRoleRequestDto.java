package com.lcp.acl.dto;

import com.lcp.acl.constants.enums.RoleTypeConstant;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class CreateRoleRequestDto {
    private String id;

    @NotNull
    @Length(min = 1, max = 50, message = "Role name must not be empty!")
    private String name;

    private String description;

    @NotNull
    private RoleTypeConstant type;

    @NotNull(message = "Actor must not be empty!")
    @Length(min = 1, message = "Actor must not be empty!")
    private String actors; // Comma separated list of actor ids, can be userId, or groupId
}
