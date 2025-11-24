package com.lcp.setting.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.lcp.common.enumeration.GeneralStatus;

import lombok.Data;

@Data
public class UnlocoRequestDto {

    @NotBlank(message = "City name is required")
    @NotNull(message = "City name is required")
    private String cityName;

    @NotBlank(message = "City code is required")
    @NotNull(message = "City code is required")
    private String cityCode;

    private GeneralStatus status = GeneralStatus.ACTIVE;
}
