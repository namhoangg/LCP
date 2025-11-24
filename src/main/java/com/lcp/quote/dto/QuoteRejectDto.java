package com.lcp.quote.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class QuoteRejectDto {
    @NotNull
    @NotEmpty
    @NotBlank
    private String reason;
}
