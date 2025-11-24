package com.lcp.common.enumeration;

public enum ValidationLevel {
  UNSPECIFIED(0),
  WARNING(1),
  ERROR(2),
  INFO(3);

  private final int value;

  ValidationLevel(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
