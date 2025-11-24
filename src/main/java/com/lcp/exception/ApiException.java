package com.lcp.exception;


import com.lcp.common.ApiMessageBase;
import com.lcp.common.Constant;
import com.lcp.common.ErrorDetail;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int code;
    private final String message;
    private final ErrorDetail[] details;

    public ApiException(ApiMessageBase apiMessage) {
        super(apiMessage.getMessage());
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
        this.details = new ErrorDetail[0];
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = new ErrorDetail[0];
    }

    public ApiException(int code, String message, ErrorDetail[] details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ApiException(ErrorDetail[] details) {
        super(Constant.VALIDATION_FAILED);
        this.code = Constant.CODE_BAD_REQUEST;
        this.message = Constant.VALIDATION_FAILED;
        this.details = details;
    }

    public ApiException(String message) {
        super(message);
        this.code = Constant.CODE_BAD_REQUEST;
        this.message = message;
        this.details = new ErrorDetail[0];
    }
}
