package com.lcp.acl.mapper;

import com.lcp.acl.dto.ResourceDto;
import com.lcp.acl.entity.Resource;

import java.util.stream.Collectors;

public class ResourceMapper {
    public static ResourceDto createResourceDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId().toString());
        resourceDto.setName(resource.getName());
        resourceDto.setParent_id(resource.getParent() != null ? resource.getParent().getId().toString() : null);

        if (resource.getChildren() != null && !resource.getChildren().isEmpty()) {
            resourceDto.setChildren(resource.getChildren().stream()
                    .map(ResourceMapper::createResourceDto)
                    .collect(Collectors.toList()));
        } else {
            resourceDto.setChildren(null);
        }

        return resourceDto;
    }

    // Not include children
    public static ResourceDto createResp(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId().toString());
        resourceDto.setName(resource.getName());
        resourceDto.setParent_id(resource.getParent() != null ? resource.getParent().getId().toString() : null);
        resourceDto.setChildren(null);

        return resourceDto;
    }
}
