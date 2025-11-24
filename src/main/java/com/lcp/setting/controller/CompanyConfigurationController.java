package com.lcp.setting.controller;

import com.lcp.common.Response;
import com.lcp.setting.dto.CompanyConfigurationRequestDto;
import com.lcp.setting.dto.CompanyConfigurationResponseDto;
import com.lcp.setting.service.CompanyConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company-configuration")
@RequiredArgsConstructor
public class CompanyConfigurationController {
    private final CompanyConfigurationService companyConfigurationService;

    @GetMapping(value = "")
    public Response<CompanyConfigurationResponseDto> getConfiguration() {
        return Response.success(companyConfigurationService.getConfiguration());
    }

    @PutMapping(value = "")
    public Response<CompanyConfigurationResponseDto> updateConfiguration(@RequestBody CompanyConfigurationRequestDto request) {
        return Response.success(companyConfigurationService.updateConfiguration(request));
    }
}