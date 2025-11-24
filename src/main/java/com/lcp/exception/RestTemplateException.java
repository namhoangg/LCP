package com.lcp.exception;

import lombok.Getter;

@Getter
public class RestTemplateException extends RuntimeException {
    private final int code;
    private final String message;

    public RestTemplateException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
