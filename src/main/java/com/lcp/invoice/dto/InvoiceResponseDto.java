package com.lcp.invoice.dto;

import com.lcp.common.enumeration.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceResponseDto {
    private Long id;
    private String code;
    private Long shipmentId;
    private Long currencyId;
    private String currencyCode;
    private BigDecimal taxRate;
    private LocalDate dueDate;
    // total freight with profit
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmountWithTax;
    // total service with profit
    private BigDecimal totalServiceAmount;
    private BigDecimal taxServiceAmount;
    private BigDecimal totalServiceAmountWithTax;
    // total freight cost
    private BigDecimal totalFreightCost;
    private BigDecimal totalFreightCostWithTax;
    // total service cost
    private BigDecimal totalServiceCost;
    private BigDecimal totalServiceCostWithTax;
    // total revenue
    private BigDecimal totalRevenue;
    // total cost
    private BigDecimal totalCost;

    private BigDecimal originalAmount;
    private BigDecimal adjustedAmount;
    private BigDecimal totalAmountDue;

    private InvoiceStatus paymentStatus;
    private BigDecimal exchangeRate;
}
