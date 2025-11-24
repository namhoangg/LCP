package com.lcp.markup.repository.custom;

import com.lcp.markup.dto.PriceMarkupListRequest;
import com.lcp.markup.entity.PriceMarkup;
import org.springframework.data.domain.Page;

public interface PriceMarkupRepositoryCustom {
    Page<PriceMarkup> list(PriceMarkupListRequest request);
}
