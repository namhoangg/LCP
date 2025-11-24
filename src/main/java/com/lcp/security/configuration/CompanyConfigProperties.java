package com.lcp.security.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "company-config")
@Data
public class CompanyConfigProperties {
    private String defaultCompanyName;
}
