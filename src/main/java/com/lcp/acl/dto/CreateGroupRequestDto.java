package com.lcp.acl.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class CreateGroupRequestDto {
    @NotNull
    @Length(min = 1, max = 50, message = "Group name must not be empty!")
    private String name;

    private String description;
}
