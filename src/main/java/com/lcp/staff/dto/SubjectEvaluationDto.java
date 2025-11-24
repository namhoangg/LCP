package com.lcp.staff.dto;

import com.lcp.acl.dto.ActionDto;
import com.lcp.acl.dto.ResourceDto;
import lombok.Data;

@Data
public class SubjectEvaluationDto {
    ActionDto action;
    ResourceDto resource; // Not include children
    Integer scope;
}
