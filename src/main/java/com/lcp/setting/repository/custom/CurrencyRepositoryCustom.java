package com.lcp.setting.repository.custom;

import com.lcp.setting.dto.CurrencyListRequest;
import com.lcp.setting.entity.Currency;
import org.springframework.data.domain.Page;

public interface CurrencyRepositoryCustom {
    Page<Currency> list(CurrencyListRequest request);
}
