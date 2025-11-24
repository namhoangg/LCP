package com.lcp.client.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiClientMessage extends ApiMessageBase {
    public static ApiClientMessage
            CLIENT_CREATE_SUCCESS = new ApiClientMessage("client.create_success"),
            CLIENT_UPDATE_SUCCESS = new ApiClientMessage("client.update_success"),
            CLIENT_DELETE_SUCCESS = new ApiClientMessage("client.delete_success"),
            CLIENT_NOT_FOUND = new ApiClientMessage("client.not_found");

    public ApiClientMessage(String message) {
        super(message);
    }
}
