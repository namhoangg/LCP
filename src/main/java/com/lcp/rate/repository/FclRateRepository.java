package com.lcp.rate.repository;

import com.lcp.rate.entity.FclRate;
import com.lcp.rate.repository.custom.FclRateRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FclRateRepository extends JpaRepository<FclRate, Long>, FclRateRepositoryCustom {
    FclRate findByProviderId(Long providerId);

    void deleteByProviderId(Long providerId);

    @Query("SELECT fr FROM FclRate fr WHERE fr.providerId IN :providerIds " +
            "AND fr.originId = :originId AND fr.destinationId = :destinationId " +
            "AND (fr.validFrom IS NULL OR fr.validFrom <= :validFrom) " +
            "AND (fr.validTo IS NULL OR fr.validTo >= :validTo)")
    List<FclRate> findByProviderIdAndValidFromAndValidToAndOriginIdAndDestinationId(List<Long> providerIds, LocalDate validFrom, LocalDate validTo, Long originId, Long destinationId);
}
