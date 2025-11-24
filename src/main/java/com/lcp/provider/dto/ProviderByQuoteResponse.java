package com.lcp.provider.dto;

import com.lcp.rate.dto.FclRateResponseDto;
import lombok.Data;

@Data
public class ProviderByQuoteResponse {
    private ProviderResponseDto provider;
    private FclRateResponseDto fclRate;
}
