package com.lcp.payment.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiPaymentMessage extends ApiMessageBase {
    public static ApiPaymentMessage
            PAYMENT_CREATE_SUCCESS = new ApiPaymentMessage("payment.create_success"),
            PAYMENT_CAPTURE_SUCCESS = new ApiPaymentMessage("payment.capture_success"),
            PAYMENT_SEND_PAYMENT_REQUEST_SUCCESS = new ApiPaymentMessage("payment.send_payment_request_success"),
            PAYMENT_NOT_FOUND = new ApiPaymentMessage("payment.not_found"),
            PAYMENT_ALREADY_PAID = new ApiPaymentMessage("payment.already_paid"),
            PAYMENT_STATUS_UPDATE_SUCCESS = new ApiPaymentMessage("payment.status_update_success");

    public ApiPaymentMessage(String message) {
        super(message);
    }
}
