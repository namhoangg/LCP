package com.lcp.setting.service;

import com.lcp.setting.dto.CompanyConfigurationRequestDto;
import com.lcp.setting.dto.CompanyConfigurationResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface CompanyConfigurationService {
    CompanyConfigurationResponseDto getConfiguration();

    CompanyConfigurationResponseDto updateConfiguration(CompanyConfigurationRequestDto request);
}