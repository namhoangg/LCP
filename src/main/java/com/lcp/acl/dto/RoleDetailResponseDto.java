package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.staff.dto.StaffResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class RoleDetailResponseDto {
    String id;
    String name;
    String description;
    RoleTypeConstant type;
    List<StaffResponseDto> staffs;
    List<GetUsersByGroupResponseDto> groups;

    @JsonProperty("total_staffs")
    Long totalStaffs;
}
