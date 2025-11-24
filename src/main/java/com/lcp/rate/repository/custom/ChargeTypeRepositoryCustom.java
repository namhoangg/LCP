package com.lcp.rate.repository.custom;

import com.lcp.rate.dto.ChargeTypeListRequest;
import com.lcp.rate.entity.ChargeType;
import org.springframework.data.domain.Page;

public interface ChargeTypeRepositoryCustom {
    Page<ChargeType> list(ChargeTypeListRequest request);
}
