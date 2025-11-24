package com.lcp.quote.dto;

import java.util.List;

import lombok.Data;

@Data
public class CargoChargeMarkup {
  private Long providerId;
  private List<CargoChargeMarkupItem> cargoChargeMarkupItems;
}
