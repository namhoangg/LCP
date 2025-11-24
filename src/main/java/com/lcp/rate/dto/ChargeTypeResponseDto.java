package com.lcp.rate.dto;

import com.lcp.rate.enumration.CalculationType;
import lombok.Data;

@Data
public class ChargeTypeResponseDto {
    private Long id;
    private String name;
    private CalculationType calculationType;
    private String description;
}
