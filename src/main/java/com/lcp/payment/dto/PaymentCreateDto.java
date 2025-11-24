package com.lcp.payment.dto;

import com.lcp.common.enumeration.PaymentMethod;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentCreateDto {
    @NotNull
    private Long invoiceId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private PaymentMethod paymentMethod;
    @NotNull
    private BigDecimal exchangeRate;
    @NotNull
    private Long currencyId;
    private String note;
}
