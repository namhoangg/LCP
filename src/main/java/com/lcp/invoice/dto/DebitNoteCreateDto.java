package com.lcp.invoice.dto;

import com.lcp.common.enumeration.NoteType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DebitNoteCreateDto {
    @NotNull
    private NoteType noteType;
    @NotNull
    private Long invoiceId;
    private BigDecimal amount;
    private String description;
}
