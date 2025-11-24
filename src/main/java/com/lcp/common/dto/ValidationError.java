package com.lcp.common.dto;

import com.lcp.common.enumeration.ValidationLevel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
  private String key;
  private String name;
  private String value;
  private String error;
  private int level;
}
