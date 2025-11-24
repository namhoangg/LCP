package com.lcp.staff.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StaffSignUpDto {
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;

    @NotNull
    @Length(min = 6, message = "Password must be at least 6 characters long")
    private String password;

}
