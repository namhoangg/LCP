package com.lcp.rate.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiRateMessage extends ApiMessageBase {
    public static ApiRateMessage
            FCL_RATE_CREATE_SUCCESS = new ApiRateMessage("rate.create_success"),
            FCL_RATE_UPDATE_SUCCESS = new ApiRateMessage("rate.update_success"),
            FCL_RATE_DELETE_SUCCESS = new ApiRateMessage("rate.delete_success"),
            FCL_RATE_NOT_FOUND = new ApiRateMessage("rate.not_found"),

            CHARGE_TYPE_CREATE_SUCCESS = new ApiRateMessage("charge_type.create_success"),
            CHARGE_TYPE_UPDATE_SUCCESS = new ApiRateMessage("charge_type.update_success"),
            CHARGE_TYPE_DELETE_SUCCESS = new ApiRateMessage("charge_type.delete_success"),
            CHARGE_TYPE_NOT_FOUND = new ApiRateMessage("charge_type.not_found"),

            SERVICE_CHARGE_CREATE_SUCCESS = new ApiRateMessage("service_charge.create_success"),
            SERVICE_CHARGE_UPDATE_SUCCESS = new ApiRateMessage("service_charge.update_success"),
            SERVICE_CHARGE_DELETE_SUCCESS = new ApiRateMessage("service_charge.delete_success"),
            SERVICE_CHARGE_NOT_FOUND = new ApiRateMessage("service_charge.not_found");




    public ApiRateMessage(String message) {
        super(message);
    }
}
