package com.lcp.invoice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitNoteUpdateDto {
    private BigDecimal amount;
    private String description;
}
