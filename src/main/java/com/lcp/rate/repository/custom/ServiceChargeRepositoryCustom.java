package com.lcp.rate.repository.custom;

import com.lcp.rate.dto.ServiceChargeListRequest;
import com.lcp.rate.entity.ServiceCharge;
import org.springframework.data.domain.Page;

public interface ServiceChargeRepositoryCustom {
    Page<ServiceCharge> list(ServiceChargeListRequest request);
}
