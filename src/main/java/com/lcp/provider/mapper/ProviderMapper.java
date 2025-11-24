package com.lcp.provider.mapper;

import com.lcp.provider.dto.ProviderCreateDto;
import com.lcp.provider.dto.ProviderResponseDto;
import com.lcp.provider.entity.Provider;
import com.lcp.provider.entity.Provider_;
import com.lcp.util.MapUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ProviderMapper {
    public static final List<String> DetailIncludeFields = List.of(
            Provider_.COMPANY_INFO,
            Provider_.CONTACT_PERSON_INFO
    );

    public static Provider createEntity(ProviderCreateDto createDto) {
        Provider provider = new Provider();
        MapUtil.copyProperties(createDto, provider);
        return provider;
    }

    public static ProviderResponseDto createResponse(Provider entity, List<String> includeFields) {
        ProviderResponseDto responseDto = new ProviderResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (CollectionUtils.isNotEmpty(includeFields)) {
            if (includeFields.contains(Provider_.COMPANY_INFO) && entity.getCompanyInfo() != null) {
                responseDto.setCompanyInfo(CompanyInfoMapper.createResponse(entity.getCompanyInfo()));
            }
            if (includeFields.contains(Provider_.CONTACT_PERSON_INFO) && entity.getContactPersonInfo() != null) {
                responseDto.setContactPersonInfo(ContactPersonInfoMapper.createResponse(entity.getContactPersonInfo()));
            }
        }
        return responseDto;
    }
}
