package com.lcp.invoice.dto;

import com.lcp.common.enumeration.InvoiceStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceUpdateStatusDto {
    InvoiceStatus paymentStatus;
    
    LocalDate dueDate;
}
