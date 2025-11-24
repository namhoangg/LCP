package com.lcp.acl.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResourceDto {
    private String id;
    private String name;
    private String parent_id;
    private List<ResourceDto> children;
}
