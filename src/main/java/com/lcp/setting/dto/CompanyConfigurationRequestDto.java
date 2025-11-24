package com.lcp.setting.dto;

import lombok.Data;

@Data
public class CompanyConfigurationRequestDto {
    private Long id;
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String landingPageImage;
    private String companyLogo;
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String youtubeUrl;
    private String tiktokUrl;
    private String linkedinUrl;
}