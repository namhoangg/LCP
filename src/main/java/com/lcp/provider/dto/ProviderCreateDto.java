package com.lcp.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.provider.enumeration.ProviderType;
import lombok.Data;

@Data
public class ProviderCreateDto {
    private String name;
    private String code;
    private ProviderType type;
    private String trackingUrl;
    @JsonIgnore
    private Long companyInfoId;
    @JsonIgnore
    private Long contactPersonInfoId;
    private CompanyInfoCreateDto companyInfo;
    private ContactPersonInfoCreateDto contactPersonInfo;
}
