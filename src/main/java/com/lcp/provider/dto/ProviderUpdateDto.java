package com.lcp.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.provider.enumeration.ProviderType;
import lombok.Data;

@Data
public class ProviderUpdateDto {
    private String name;
    private String code;
    private ProviderType type;
    private String trackingUrl;
    private CompanyInfoUpdateDto companyInfo;
    private ContactPersonInfoUpdateDto contactPersonInfo;
}
