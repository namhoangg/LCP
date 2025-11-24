package com.lcp.invoice.repository.custom;

import com.lcp.invoice.dto.InvoiceListRequest;
import com.lcp.invoice.entity.Invoice;
import org.springframework.data.domain.Page;

public interface InvoiceRepositoryCustom {
    Page<Invoice> list(InvoiceListRequest request);
}
