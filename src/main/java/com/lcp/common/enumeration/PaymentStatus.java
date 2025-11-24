package com.lcp.common.enumeration;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    CANCELLED,
    UNPAID,
    REQUESTED,
    PAID
}
