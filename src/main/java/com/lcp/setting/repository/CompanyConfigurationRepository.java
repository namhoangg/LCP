package com.lcp.setting.repository;

import com.lcp.setting.entity.CompanyConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyConfigurationRepository extends JpaRepository<CompanyConfiguration, Long> {
}