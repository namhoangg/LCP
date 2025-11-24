package com.lcp.payment.dto;

import com.lcp.common.enumeration.PaymentStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentUpdateDto {
    @NotNull
    private PaymentStatus paymentStatus;
}
