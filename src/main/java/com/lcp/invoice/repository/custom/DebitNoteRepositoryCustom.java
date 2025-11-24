package com.lcp.invoice.repository.custom;


import com.lcp.invoice.dto.DebitNoteListRequest;
import com.lcp.invoice.entity.DebitNote;
import org.springframework.data.domain.Page;

public interface DebitNoteRepositoryCustom {
    Page<DebitNote> list(DebitNoteListRequest request);
}
