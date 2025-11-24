package com.lcp.rate.repository;

import java.util.List;

import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.rate.entity.GoodKind;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodKindRepository extends JpaRepository<GoodKind, Long> {
    List<GoodKind> findAllByStatus(GeneralStatus status);
}
