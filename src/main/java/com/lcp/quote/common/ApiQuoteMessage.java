package com.lcp.quote.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiQuoteMessage extends ApiMessageBase {
    public static ApiQuoteMessage
            QUOTE_CREATE_SUCCESS = new ApiQuoteMessage("quote.create_success"),
            QUOTE_UPDATE_SUCCESS = new ApiQuoteMessage("quote.update_success"),
            QUOTE_DELETE_SUCCESS = new ApiQuoteMessage("quote.delete_success"),
            QUOTE_NOT_FOUND = new ApiQuoteMessage("quote.not_found"),
            QUOTE_REQUEST_CREATE_SUCCESS = new ApiQuoteMessage("quote.request.create_success"),
            QUOTE_REQUEST_UPDATE_SUCCESS = new ApiQuoteMessage("quote.request.update_success"),
            QUOTE_REQUEST_DELETE_SUCCESS = new ApiQuoteMessage("quote.request.delete_success"),
            QUOTE_REQUEST_NOT_FOUND = new ApiQuoteMessage("quote.request.not_found");
    public ApiQuoteMessage(String message) {
        super(message);
    }
}
