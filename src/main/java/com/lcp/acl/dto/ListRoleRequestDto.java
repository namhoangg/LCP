package com.lcp.acl.dto;

import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.common.dto.BaseListRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListRoleRequestDto extends BaseListRequest {
    private RoleTypeConstant type;
}
