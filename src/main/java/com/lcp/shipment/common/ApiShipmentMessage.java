package com.lcp.shipment.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiShipmentMessage extends ApiMessageBase {
    public static ApiShipmentMessage
            SHIPMENT_CREATE_SUCCESS = new ApiShipmentMessage("shipment.create_success"),
            SHIPMENT_UPDATE_SUCCESS = new ApiShipmentMessage("shipment.update_success"),
            SHIPMENT_DELETE_SUCCESS = new ApiShipmentMessage("shipment.delete_success"),
            SHIPMENT_NOT_FOUND = new ApiShipmentMessage("shipment.not_found"),
            CONTAINER_UPSERT_SUCCESS = new ApiShipmentMessage("container.upsert_success"),
            CONTAINER_DELETE_SUCCESS = new ApiShipmentMessage("container.delete_success");

    public ApiShipmentMessage(String message) {
        super(message);
    }
}
