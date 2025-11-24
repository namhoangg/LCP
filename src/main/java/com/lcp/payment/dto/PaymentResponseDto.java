package com.lcp.payment.dto;

import com.lcp.common.enumeration.PaymentMethod;
import com.lcp.common.enumeration.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class PaymentResponseDto {
    private Long id;
    private Long invoiceId;
    private Long shipmentId;
    private String code;
    private BigDecimal amount;
    private BigDecimal fee;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private OffsetDateTime paymentDate;
    private PaymentStatus paymentStatus;
    private String note;
    private BigDecimal exchangeRate;
    private Long currencyId;
    private String approvalUrl;
    private Map<String, Object> paypalResponse;
}
