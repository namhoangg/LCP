package com.lcp.staff.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class StaffForgotPasswordDto {
    @NotNull
    @NotEmpty
    @Email
    private String email;
}
