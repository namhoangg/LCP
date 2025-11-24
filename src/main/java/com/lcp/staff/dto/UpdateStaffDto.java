package com.lcp.staff.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateStaffDto {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String firstname;

    @NotNull
    @NotEmpty
    private String lastname;

    @NotNull
    @NotEmpty
    private String phone;
}