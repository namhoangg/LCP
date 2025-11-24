package com.lcp.acl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdditionalDataDto {
    private String description;

    @JsonProperty("resource_action_id")
    private String resourceActionId;

    public AdditionalDataDto(String description, String s) {
        this.description = description;
        this.resourceActionId = s;
    }
}
