package com.lcp.setting.repository;

import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.repository.custom.UnlocoRepositoryCustom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnlocoRepository extends JpaRepository<Unloco, Long>, UnlocoRepositoryCustom {
  Optional<Unloco> findByCityCodeAndStatus(String cityCode, GeneralStatus status);
}
