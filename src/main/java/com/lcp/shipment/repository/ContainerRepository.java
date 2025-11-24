package com.lcp.shipment.repository;

import com.lcp.shipment.entity.Container;
import com.lcp.shipment.repository.custom.ContainerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container, Long>, ContainerRepositoryCustom {
    @Query("FROM Container WHERE shipmentId = :shipmentId ORDER BY id ASC")
    List<Container> findByShipmentId(Long shipmentId);
}
