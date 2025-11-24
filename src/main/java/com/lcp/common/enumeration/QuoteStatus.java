package com.lcp.common.enumeration;

import lombok.Getter;


@Getter
public enum QuoteStatus {
    DRAFT,
    REQUESTED,
    CREATED,
    ACCEPTED,
    STAFF_DRAFT,
    BOOKED, // Shipment created, quote is can not change anymore, but can create another shipment with this quotes
    REJECTED,
    OVERDUE
}

//staff can edit if it staff draft, request, created, can not edit if it accepted, booked, rejected