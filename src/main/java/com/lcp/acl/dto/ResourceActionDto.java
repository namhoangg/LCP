package com.lcp.acl.dto;

import lombok.Data;

@Data
public class ResourceActionDto {
    private ResourceDto resource;
    private ActionDto action;
    private String description;
    private int id;
}
