package com.lcp.provider.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import io.micrometer.core.lang.NonNull;
import lombok.Data;

@Data
public class GetByQuoteRequest {
    @NonNull
    private Long originId;
    @NonNull
    private Long destinationId;
    @NonNull
    @NotEmpty
    private String containerTypeIds;
    @NonNull
    private LocalDate etd;
}
