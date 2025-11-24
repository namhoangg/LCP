package com.lcp.exchangerate.repository;

import com.lcp.exchangerate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByFromCurrencyIdAndToCurrencyIdAndCreatedDate(Long fromCurrencyId, Long toCurrencyId, LocalDate createdDate);
}
