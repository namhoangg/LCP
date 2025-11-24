package com.lcp.setting.service.impl;

import com.lcp.security.configuration.CompanyConfigProperties;
import com.lcp.setting.dto.CompanyConfigurationRequestDto;
import com.lcp.setting.dto.CompanyConfigurationResponseDto;
import com.lcp.setting.entity.CompanyConfiguration;
import com.lcp.setting.repository.CompanyConfigurationRepository;
import com.lcp.setting.service.CompanyConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyConfigurationServiceImpl implements CompanyConfigurationService {
    private final CompanyConfigurationRepository companyConfigurationRepository;
    private final CompanyConfigProperties companyConfigProperties;

    @Override
    @Transactional
    public CompanyConfigurationResponseDto getConfiguration() {
        List<CompanyConfiguration> configurations = companyConfigurationRepository.findAll();
        if (configurations.isEmpty()) {
            // If no configuration exists, create a new one with default values
            CompanyConfiguration defaultConfiguration = new CompanyConfiguration();
            defaultConfiguration.setCompanyName(companyConfigProperties.getDefaultCompanyName());
            companyConfigurationRepository.save(defaultConfiguration);
            return CompanyConfigurationResponseDto.from(defaultConfiguration);
        }

        return CompanyConfigurationResponseDto.from(configurations.get(0));
    }

    @Override
    @Transactional
    public CompanyConfigurationResponseDto updateConfiguration(CompanyConfigurationRequestDto request) {
        CompanyConfiguration configuration = companyConfigurationRepository.findById(request.getId())
                .orElseGet(CompanyConfiguration::new);

        // Update the configuration with values from the request
        configuration.setCompanyName(request.getCompanyName());
        configuration.setCompanyAddress(request.getCompanyAddress());
        configuration.setCompanyPhone(request.getCompanyPhone());
        configuration.setCompanyEmail(request.getCompanyEmail());
        configuration.setLandingPageImage(request.getLandingPageImage());
        configuration.setCompanyLogo(request.getCompanyLogo());
        configuration.setFacebookUrl(request.getFacebookUrl());
        configuration.setInstagramUrl(request.getInstagramUrl());
        configuration.setTwitterUrl(request.getTwitterUrl());
        configuration.setYoutubeUrl(request.getYoutubeUrl());
        configuration.setTiktokUrl(request.getTiktokUrl());
        configuration.setLinkedinUrl(request.getLinkedinUrl());

        // Save the updated configuration
        CompanyConfiguration savedConfiguration = companyConfigurationRepository.save(configuration);

        return CompanyConfigurationResponseDto.from(savedConfiguration);
    }
}