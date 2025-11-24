package com.lcp.advancesearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lcp.common.enumeration.OperandType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdvanceSearchResponseDto implements Serializable {
    private String columnName;
    private OperandType operandType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> enums;
}
