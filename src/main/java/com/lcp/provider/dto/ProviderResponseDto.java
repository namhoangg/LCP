package com.lcp.provider.dto;

import com.lcp.provider.enumeration.ProviderType;
import lombok.Data;

@Data
public class ProviderResponseDto {
    private Long id;
    private String name;
    private String code;
    private ProviderType type;
    private String trackingUrl;
    private ContactPersonInfoResponseDto contactPersonInfo;
    private CompanyInfoResponseDto companyInfo;

}
