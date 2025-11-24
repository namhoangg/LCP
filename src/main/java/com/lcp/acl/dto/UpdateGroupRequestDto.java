package com.lcp.acl.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateGroupRequestDto {
    @NotEmpty
    @NotNull
    private String id;

    @NotNull
    @Length(min = 1, max = 50, message = "Group name must not be empty!")
    private String name;

    private String description;
}
