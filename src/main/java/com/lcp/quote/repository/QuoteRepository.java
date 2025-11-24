package com.lcp.quote.repository;

import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.repository.custom.QuoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long>, QuoteRepositoryCustom {
    List<Quote> findAllByClientIdAndQuoteStatusInAndValidUntilGreaterThanEqual(Long clientId, Collection<QuoteStatus> quoteStatus, LocalDate validUntil);

    @Query("UPDATE Quote q set q.quoteStatus = 'OVERDUE' where q.validUntil < now() and q.quoteStatus = 'ACCEPTED'")
    @Modifying
    @Transactional
    void updatePaymentStatusToOverdue();
}
