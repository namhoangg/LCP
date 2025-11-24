package com.lcp.provider.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiProviderMessage extends ApiMessageBase {
    public static ApiProviderMessage
            PROVIDER_CREATE_SUCCESS = new ApiProviderMessage("provider.create_success"),
            PROVIDER_UPDATE_SUCCESS = new ApiProviderMessage("provider.update_success"),
            PROVIDER_DELETE_SUCCESS = new ApiProviderMessage("provider.delete_success"),
            PROVIDER_NOT_FOUND = new ApiProviderMessage("provider.not_found");

    public ApiProviderMessage(String message) {
        super(message);
    }
}
