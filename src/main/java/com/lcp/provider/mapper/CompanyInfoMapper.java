package com.lcp.provider.mapper;

import com.lcp.provider.dto.CompanyInfoCreateDto;
import com.lcp.provider.dto.CompanyInfoResponseDto;
import com.lcp.provider.entity.CompanyInfo;
import com.lcp.util.MapUtil;

public class CompanyInfoMapper {
    public static CompanyInfo createEntity(CompanyInfoCreateDto createDto) {
        CompanyInfo companyInfo = new CompanyInfo();
        MapUtil.copyProperties(createDto, companyInfo);
        return companyInfo;
    }

    public static CompanyInfoResponseDto createResponse(CompanyInfo entity) {
        CompanyInfoResponseDto responseDto = new CompanyInfoResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
