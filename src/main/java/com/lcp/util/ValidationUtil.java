package com.lcp.util;

import com.lcp.common.dto.ValidationError;
import com.lcp.common.enumeration.ValidationLevel;

public class ValidationUtil {
  public static ValidationError createValidationError(String key, String name, String value, String error, int level) {
    return new ValidationError(key, name, value, error, level);
  }
}
