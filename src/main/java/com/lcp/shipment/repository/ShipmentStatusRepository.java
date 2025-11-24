package com.lcp.shipment.repository;

import com.lcp.shipment.entity.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Long> {
}
