package com.lcp.rate.repository.custom;

import com.lcp.rate.dto.FclRateListRequest;
import com.lcp.rate.entity.FclRate;
import org.springframework.data.domain.Page;

public interface FclRateRepositoryCustom {
    Page<FclRate> list(FclRateListRequest request);
}
