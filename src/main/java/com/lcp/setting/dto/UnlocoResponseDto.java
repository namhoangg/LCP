package com.lcp.setting.dto;

import com.lcp.common.enumeration.GeneralStatus;
import lombok.Data;

@Data
public class UnlocoResponseDto {
    private Long id;
    private String code;
    private String cityName;
    private String cityCode;
    private String countryCode;
    private String countryName;
    private String displayName;
    private GeneralStatus status;
}
