package com.lcp.dashboard.dto;

import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.common.enumeration.ShipmentStatusEnum;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardAdminResponse {
    private Long activeShipment;
    private Long deliveredShipment;
    private Long quoteAccepted;
    private Long quoteBooked;
    private Long overdueQuote;
//    private Long requestPending;
    private Long overdueInvoice;
    private Long paidInvoice;

//    private Map<QuoteStatus, Long> quoteStatusCount;
//    private Map<ShipmentStatusEnum, Long> shipmentStatusCount;
    private Map<Long, Long> providerQuoteCount;
    private Map<Long, Long> providerShipmentCount;
    private Map<Long, Long> clientQuoteCount;
    private Map<Long, Long> clientShipmentCount;
}
