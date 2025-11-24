package com.lcp.invoice.repository;

import com.lcp.invoice.entity.DebitNote;
import com.lcp.invoice.repository.custom.DebitNoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebitNoteRepository extends JpaRepository<DebitNote, Long>, DebitNoteRepositoryCustom {
    List<DebitNote> findByInvoiceId(Long invoiceId);
}
