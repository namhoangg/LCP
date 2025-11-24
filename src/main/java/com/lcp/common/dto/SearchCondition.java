package com.lcp.common.dto;

import com.lcp.common.enumeration.OperandType;
import com.lcp.common.enumeration.OperatorType;
import lombok.Data;

import java.util.List;

@Data
public class SearchCondition {

    private String fieldName;
    private OperandType operandType;
    private OperatorType operatorType;
    private String data;
    private List<String> datas;
}
