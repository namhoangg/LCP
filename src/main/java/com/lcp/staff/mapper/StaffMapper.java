package com.lcp.staff.mapper;


import com.lcp.staff.dto.StaffResponseDto;
import com.lcp.staff.dto.StaffSignUpDto;
import com.lcp.staff.entity.Staff;
import com.lcp.util.MapUtil;

public class StaffMapper {
    public static Staff createEntity(StaffSignUpDto createDto) {
        Staff entity = new Staff();
        MapUtil.copyProperties(createDto, entity);
        return entity;
    }

    public static StaffResponseDto createResponse(Staff entity) {
        StaffResponseDto responseDto = new StaffResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        return responseDto;
    }
}
