package com.lcp.rate.repository;

import com.lcp.rate.entity.ServiceCharge;
import com.lcp.rate.repository.custom.ServiceChargeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceChargeRepository extends JpaRepository<ServiceCharge, Long>, ServiceChargeRepositoryCustom {
}
