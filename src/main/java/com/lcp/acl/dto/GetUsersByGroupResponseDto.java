package com.lcp.acl.dto;

import com.lcp.staff.dto.StaffResponseDto;
import lombok.Data;

@Data
public class GetUsersByGroupResponseDto {
    private GroupResponseDto group;

    private StaffResponseDto[] users;
}
