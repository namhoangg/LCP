package com.lcp.setting.dto;

import com.lcp.setting.entity.CompanyConfiguration;
import lombok.Data;

@Data
public class CompanyConfigurationResponseDto {
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

    public static CompanyConfigurationResponseDto from(CompanyConfiguration entity) {
        CompanyConfigurationResponseDto dto = new CompanyConfigurationResponseDto();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCompanyAddress(entity.getCompanyAddress());
        dto.setCompanyPhone(entity.getCompanyPhone());
        dto.setCompanyEmail(entity.getCompanyEmail());
        dto.setLandingPageImage(entity.getLandingPageImage());
        dto.setCompanyLogo(entity.getCompanyLogo());
        dto.setFacebookUrl(entity.getFacebookUrl());
        dto.setInstagramUrl(entity.getInstagramUrl());
        dto.setTwitterUrl(entity.getTwitterUrl());
        dto.setYoutubeUrl(entity.getYoutubeUrl());
        dto.setTiktokUrl(entity.getTiktokUrl());
        dto.setLinkedinUrl(entity.getLinkedinUrl());
        return dto;
    }
}