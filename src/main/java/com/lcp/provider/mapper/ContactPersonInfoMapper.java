package com.lcp.provider.mapper;

import com.lcp.provider.dto.ContactPersonInfoCreateDto;
import com.lcp.provider.dto.ContactPersonInfoResponseDto;
import com.lcp.provider.entity.ContactPersonInfo;
import com.lcp.util.MapUtil;

public class ContactPersonInfoMapper {
    public static ContactPersonInfo createEntity(ContactPersonInfoCreateDto createDto) {
        ContactPersonInfo contactPersonInfo = new ContactPersonInfo();
        MapUtil.copyProperties(createDto, contactPersonInfo);
        return contactPersonInfo;
    }

    public static ContactPersonInfoResponseDto createResponse(ContactPersonInfo entity) {
        ContactPersonInfoResponseDto responseDto = new ContactPersonInfoResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
