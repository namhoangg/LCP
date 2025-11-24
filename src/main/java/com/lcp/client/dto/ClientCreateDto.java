package com.lcp.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.provider.dto.CompanyInfoCreateDto;
import com.lcp.provider.dto.ContactPersonInfoCreateDto;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClientCreateDto {
    private String name;
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    @JsonIgnore
    private Long companyInfoId;
    @JsonIgnore
    private Long contactPersonInfoId;
    private CompanyInfoCreateDto companyInfo;
    private ContactPersonInfoCreateDto contactPersonInfo;

    private Long servedBy;
}
