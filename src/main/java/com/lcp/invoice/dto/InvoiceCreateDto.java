package com.lcp.invoice.dto;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class InvoiceCreateDto {
    @NotNull
    private Long shipmentId;
    @NotNull
    private BigDecimal taxRate;
    @NotNull
    private Long currencyId;
    private BigDecimal exchangeRate;

    @NotNull
    private LocalDate dueDate;
}
