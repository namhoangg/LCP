package com.lcp.client.mapper;

import com.lcp.client.dto.ClientCreateDto;
import com.lcp.client.dto.ClientResponseDto;
import com.lcp.client.entity.Client;
import com.lcp.client.entity.Client_;
import com.lcp.provider.entity.Provider_;
import com.lcp.provider.mapper.CompanyInfoMapper;
import com.lcp.provider.mapper.ContactPersonInfoMapper;
import com.lcp.staff.mapper.StaffMapper;
import com.lcp.util.MapUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class ClientMapper {
    public static final List<String> DetailIncludeFields = List.of(
            Client_.COMPANY_INFO,
            Client_.CONTACT_PERSON_INFO
    );

    public static Client createEntity(ClientCreateDto createDto) {
        Client client = new Client();
        MapUtil.copyProperties(createDto, client);
        return client;
    }

    public static ClientResponseDto createResponse(Client entity, List<String> includeFields) {
        ClientResponseDto responseDto = new ClientResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (CollectionUtils.isNotEmpty(includeFields)) {
            if (includeFields.contains(Provider_.COMPANY_INFO) && entity.getCompanyInfo() != null) {
                responseDto.setCompanyInfo(CompanyInfoMapper.createResponse(entity.getCompanyInfo()));
            }
            if (includeFields.contains(Provider_.CONTACT_PERSON_INFO) && entity.getContactPersonInfo() != null) {
                responseDto.setContactPersonInfo(ContactPersonInfoMapper.createResponse(entity.getContactPersonInfo()));
            }
        }
        if (entity.getServedByStaff() != null) {
            responseDto.setServedByStaff(StaffMapper.createResponse(entity.getServedByStaff()));
        }
        return responseDto;
    }
}
