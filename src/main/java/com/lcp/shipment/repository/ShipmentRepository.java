package com.lcp.shipment.repository;

import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.repository.custom.ShipmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>, ShipmentRepositoryCustom {
}
