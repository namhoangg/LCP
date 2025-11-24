package com.lcp.markup.common;

import com.lcp.common.ApiMessageBase;

public class ApiPriceMarkupMessage extends ApiMessageBase {

    public static ApiPriceMarkupMessage
            PRICE_MARKUP_CREATE_SUCCESS = new ApiPriceMarkupMessage("price_markup.create_success"),
            PRICE_MARKUP_UPDATE_SUCCESS = new ApiPriceMarkupMessage("price_markup.update_success"),
            PRICE_MARKUP_DELETE_SUCCESS = new ApiPriceMarkupMessage("price_markup.delete_success"),
            PRICE_MARKUP_NOT_FOUND = new ApiPriceMarkupMessage("price_markup.not_found"),
            PRICE_MARKUP_ALREADY_EXISTS = new ApiPriceMarkupMessage("price_markup.already_exists");

    public ApiPriceMarkupMessage(String message) {
        super(message);
    }
}
