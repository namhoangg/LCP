package com.lcp.shipment.helper;

import com.lcp.shipment.repository.ShipmentRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class ShipmentHelper {
    private static final String SHIPMENT_CODE_FORMAT = "%s%06d";
    private static final String SHIPMENT_CODE_PREFIX = "SH";
    private final ShipmentRepository shipmentRepository;

    public String genShipmentCode() {
        long count = shipmentRepository.count();
        return String.format(SHIPMENT_CODE_FORMAT, SHIPMENT_CODE_PREFIX, ++count);
    }
}
