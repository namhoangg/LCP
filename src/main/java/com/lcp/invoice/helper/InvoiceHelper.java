package com.lcp.invoice.helper;

import com.lcp.invoice.repository.InvoiceRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class InvoiceHelper {
    private static final String INVOICE_CODE_FORMAT = "%s%06d";
    private static final String INVOICE_CODE_PREFIX = "INV";
    private final InvoiceRepository invoiceRepository;

    public String genInvoiceCode() {
        long count = invoiceRepository.count();
        return String.format(INVOICE_CODE_FORMAT, INVOICE_CODE_PREFIX, ++count);
    }
}
