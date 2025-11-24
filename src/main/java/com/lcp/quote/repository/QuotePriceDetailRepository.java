package com.lcp.quote.repository;

import com.lcp.quote.entity.QuotePriceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotePriceDetailRepository extends JpaRepository<QuotePriceDetail, Long> {
    // Custom query methods can be defined here if needed
    // For example, find by quoteId or providerId
    List<QuotePriceDetail> findAllByQuoteId(Long quoteId);

    void deleteAllByQuoteId(Long quoteId);
}
