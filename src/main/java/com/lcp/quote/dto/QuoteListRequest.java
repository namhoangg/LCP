package com.lcp.quote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.common.dto.BaseListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class QuoteListRequest extends BaseListRequest {
    @JsonIgnore
    private Boolean isRequest;
}
