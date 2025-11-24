package com.lcp.client.dto;

import com.lcp.provider.dto.CompanyInfoUpdateDto;
import com.lcp.provider.dto.ContactPersonInfoUpdateDto;
import lombok.Data;

@Data
public class ClientUpdateDto {
    private String name;
    private CompanyInfoUpdateDto companyInfo;
    private ContactPersonInfoUpdateDto contactPersonInfo;
    private Long servedBy;
}
