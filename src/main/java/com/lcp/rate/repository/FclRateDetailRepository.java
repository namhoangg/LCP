package com.lcp.rate.repository;

import com.lcp.rate.entity.FclRateDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FclRateDetailRepository extends JpaRepository<FclRateDetail, Long> {
    void deleteByFclRateId(Long fclRateId);
}
