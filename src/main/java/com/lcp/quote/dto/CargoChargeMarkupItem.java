package com.lcp.quote.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CargoChargeMarkupItem {
  private Long containerTypeId;
  private BigDecimal markup;
}
