package com.lcp.shipment.repository.custom;

import com.lcp.shipment.dto.ShipmentListRequest;
import com.lcp.shipment.entity.Shipment;
import org.springframework.data.domain.Page;

public interface ShipmentRepositoryCustom {
    Page<Shipment> list(ShipmentListRequest request);
}
