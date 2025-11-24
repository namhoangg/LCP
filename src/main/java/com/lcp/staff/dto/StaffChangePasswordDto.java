package com.lcp.staff.dto;

import lombok.Data;

@Data
public class StaffChangePasswordDto {
    private String email;
    private String oldPassword;
    private String newPassword;
}
