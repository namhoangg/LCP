package com.lcp.setting.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "company_configuration")
public class CompanyConfiguration extends EntityBase {
    @Id
    @GeneratedValue
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
