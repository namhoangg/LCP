package com.lcp.dashboard.dto;

import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.common.enumeration.ShipmentStatusEnum;
import lombok.Data;

import java.util.Map;

@Data
public class DashboardClientResponse {
    private Long activeShipment;
    private Long requestPending;
    private Long overdueInvoice;

    private Map<QuoteStatus, Long> quoteStatusCount;
    private Map<ShipmentStatusEnum, Long> shipmentStatusCount;
}
