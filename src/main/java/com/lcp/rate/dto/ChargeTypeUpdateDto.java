package com.lcp.rate.dto;

import com.lcp.rate.enumration.CalculationType;
import lombok.Data;

@Data
public class ChargeTypeUpdateDto {
    private String name;
    private CalculationType calculationType;
    private String description;
}
