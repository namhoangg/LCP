package com.lcp.staff.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcp.client.dto.ClientResponseDto;
import lombok.Data;

@Data
public class StaffResponseDto {
    private Long id;
    private String code;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String avatar;

    @JsonProperty("is_client")
    private Boolean isClient;

    @JsonProperty("is_super_admin")
    private Boolean isSuperAdmin;

    private ClientResponseDto client;
}
