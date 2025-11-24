package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MatrixDto {
    private List<ActionDto> actions;

    @JsonProperty("additional_data")
    private Map<String, AdditionalDataDto> additionalData;

    private ResourceDto resources;

    private List<MatrixDto> children;

    public MatrixDto(ResourceDto resource, List<ActionDto> actions, Map<String, AdditionalDataDto> additionalData, List<MatrixDto> matrixFromResourceIds) {
        this.resources = resource;
        this.actions = actions;
        this.additionalData = additionalData;
        this.children = matrixFromResourceIds;
    }
}
