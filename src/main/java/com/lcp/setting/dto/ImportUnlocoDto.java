package com.lcp.setting.dto;

import lombok.Data;
import java.util.List;

import com.lcp.common.dto.ValidationError;

@Data
public class ImportUnlocoDto {
  private String cityName;
  private String cityCode;
  private List<ValidationError> errors;
}
