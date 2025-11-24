package com.lcp.staff.dto;

import lombok.Data;

@Data
public class StaffSignInResponseDto {
    private String token;
    private StaffResponseDto account;
    private Boolean forceChangePassword;
}
