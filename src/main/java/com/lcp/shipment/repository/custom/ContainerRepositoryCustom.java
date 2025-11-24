package com.lcp.shipment.repository.custom;

import com.lcp.shipment.dto.ContainerListRequest;
import com.lcp.shipment.entity.Container;
import org.springframework.data.domain.Page;

public interface ContainerRepositoryCustom {
    Page<Container> list(ContainerListRequest request);
}
