package com.lcp.invoice.dto;

import com.lcp.client.dto.ClientResponseDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoicePrintDto {
    private String code;
    // total freight with profit
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmountWithTax;
    // total service with profit
    private BigDecimal totalServiceAmount;
    private BigDecimal taxServiceAmount;
    private BigDecimal totalServiceAmountWithTax;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    private ClientResponseDto client;
}
