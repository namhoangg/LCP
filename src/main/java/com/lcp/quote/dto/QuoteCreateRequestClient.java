package com.lcp.quote.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class QuoteCreateRequestClient {
    @NotNull
    private Long originId;

    @NotNull
    private Long destinationId;

    @NotNull
    private Long goodKindId;
    private String goodDescription;
    private String note;
    private Boolean isDraft;

    @NotNull
    private LocalDate etd;

    @NotNull
    private LocalDate eta;

    @NotNull
    private List<String> containerTypeIds;
    private List<String> serviceChargeIds;
    
}
