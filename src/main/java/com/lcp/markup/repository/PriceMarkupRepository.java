package com.lcp.markup.repository;

import com.lcp.markup.entity.PriceMarkup;
import com.lcp.markup.repository.custom.PriceMarkupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceMarkupRepository extends JpaRepository<PriceMarkup, Long>, PriceMarkupRepositoryCustom {
}
