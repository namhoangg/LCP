package com.lcp.provider.repository;

import com.lcp.provider.entity.Provider;
import com.lcp.provider.repository.custom.ProviderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long>, ProviderRepositoryCustom {
}
