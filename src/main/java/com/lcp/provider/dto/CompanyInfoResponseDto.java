package com.lcp.provider.dto;

import lombok.Data;

@Data
public class CompanyInfoResponseDto {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String zipCode;
    private String taxNumber;
    private boolean isDomestic;
}
