package com.lcp.common.enumeration;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    UNPAID,
    PAID,
    PARTIAL,
    PENDING,
    OVERDUE
}
