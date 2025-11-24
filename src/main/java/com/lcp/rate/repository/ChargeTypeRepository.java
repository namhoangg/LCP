package com.lcp.rate.repository;

import com.lcp.rate.entity.ChargeType;
import com.lcp.rate.repository.custom.ChargeTypeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeTypeRepository extends JpaRepository<ChargeType, Long>, ChargeTypeRepositoryCustom {
}
