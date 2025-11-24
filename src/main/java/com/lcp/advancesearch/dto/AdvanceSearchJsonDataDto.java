package com.lcp.advancesearch.dto;

import com.lcp.common.enumeration.OperandType;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdvanceSearchJsonDataDto implements Serializable {

    private String columnName;
    private OperandType operandType;
    private String clazz;
}
