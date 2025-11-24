package com.lcp.staff.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class StaffResetPasswordDto {

    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotNull
    private String newPassword;

    @NotNull
    @NotEmpty
    private String confirmPassword;
}
