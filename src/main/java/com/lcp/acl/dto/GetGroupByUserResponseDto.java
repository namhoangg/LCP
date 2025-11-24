package com.lcp.acl.dto;

import com.lcp.staff.dto.StaffResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class GetGroupByUserResponseDto {
    private List<GroupResponseDto> groups;
    private StaffResponseDto user;
}
