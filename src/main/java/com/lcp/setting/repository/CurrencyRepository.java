package com.lcp.setting.repository;

import com.lcp.setting.entity.Currency;
import com.lcp.setting.repository.custom.CurrencyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CurrencyRepositoryCustom {
}
