package com.lcp.invoice.dto;

import com.lcp.common.enumeration.NoteType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitNoteResponseDto {
    private Long id;
    private NoteType noteType;
    private Long invoiceId;
    private Long shipmentId;
    private BigDecimal amount;
    private String description;
}
