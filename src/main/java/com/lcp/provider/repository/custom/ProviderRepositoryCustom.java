package com.lcp.provider.repository.custom;

import com.lcp.provider.dto.GetByQuoteRequest;
import com.lcp.provider.dto.ProviderListRequest;
import com.lcp.provider.entity.Provider;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProviderRepositoryCustom {
    Page<Provider> list(ProviderListRequest request);

    List<Provider> findAllByQuote(GetByQuoteRequest request);
}
