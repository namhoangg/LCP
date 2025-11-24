package com.lcp.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ShipmentStatusEnum {
    DRAFT("Draft"),
    PRE_CARGO("Pre-Cargo"),
    ORIGIN("Origin"),
    ONGOING("Ongoing"),
    DESTINATION("Destination"),
    DELIVERED("Delivered");

    private final String display;
}
