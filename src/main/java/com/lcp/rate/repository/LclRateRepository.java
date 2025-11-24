package com.lcp.rate.repository;

import com.lcp.rate.entity.LclRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LclRateRepository extends JpaRepository<LclRate, Long> {
    @Query("SELECT lr FROM LclRate lr WHERE lr.providerId IN :providerIds " +
            "AND lr.originId = :originId AND lr.destinationId = :destinationId " +
            "AND (lr.validFrom IS NULL OR lr.validFrom <= :validFrom) " +
            "AND (lr.validTo IS NULL OR lr.validTo >= :validTo)")
    List<LclRate> findByProviderIdAndValidFromAndValidToAndOriginIdAndDestinationId(
            List<Long> providerIds,
            LocalDate validFrom,
            LocalDate validTo,
            Long originId,
            Long destinationId);
}