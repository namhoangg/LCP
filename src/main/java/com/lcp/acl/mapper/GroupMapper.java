package com.lcp.acl.mapper;

import com.lcp.acl.dto.GroupResponseDto;
import com.lcp.acl.entity.StaffGroupEntity;
import com.lcp.util.MapUtil;

public class GroupMapper {
    public static GroupResponseDto createResponse(StaffGroupEntity entity) {
        GroupResponseDto responseDto = new GroupResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        responseDto.setId(entity.getId().toString());
        return responseDto;
    }
}
