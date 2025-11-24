package com.lcp.provider.dto;

import lombok.Data;

@Data
public class ContactPersonInfoUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
}
