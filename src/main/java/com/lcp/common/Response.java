package com.lcp.common;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Data
@NoArgsConstructor
public class Response<T> {

    boolean success;
    int code;
    String message;
    T payload;

    public Response(boolean success, ApiMessageBase apiMessage, T payload) {
        this.success = success;
        this.code = apiMessage.getCode();
        this.message = apiMessage.getMessage();
        this.payload = payload;
    }

    public Response(boolean success, int code, String message, T payload) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public static <T> Response<T> error(ApiMessageBase apiMessage) {
        return new Response<>(false, apiMessage, null);
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(false, code, message, null);
    }

    public static <T> Response<T> success() {
        return new Response<>(true, ApiMessageBase.SUCCESS, null);
    }

    public static <T> Response<T> success(T payload) {
        return new Response<>(true, ApiMessageBase.SUCCESS, payload);
    }

    public static <T> Response<T> success(ApiMessageBase apiMessage) {
        return new Response<>(true, apiMessage, null);
    }

    public static Response<ApiMessageBase> ofApiMessage(ApiMessageBase apiMessage) {
        return new Response<>(true, ApiMessageBase.SUCCESS, new ApiMessageBase(apiMessage.code, apiMessage.message));
    }

    public static <T> Response<T> success(T payload, ApiMessageBase apiMessage) {
        return new Response<>(true, apiMessage, payload);
    }

    public static void servletResponse(HttpServletResponse response, ApiMessageBase apiMessage) throws IOException {
        servletResponse(response, apiMessage, mapHttpStatus(apiMessage.getCode()), false);
    }

    public static void servletResponse(HttpServletResponse response, ApiMessageBase apiMessage, HttpStatus httpStatus, boolean close) throws IOException {
        String responseString = RequestHelper.convertToJson(Response.error(apiMessage));
        servletResponse(response, responseString, httpStatus, close);
    }

    public static void servletResponse(HttpServletResponse response, int code, String message) throws IOException {
        servletResponse(response, code, message, mapHttpStatus(code), false);
    }

    public static void servletResponse(HttpServletResponse response, int code, String message, HttpStatus httpStatus, boolean close) throws IOException {
        String responseString = RequestHelper.convertToJson(Response.error(code, message));
        servletResponse(response, responseString, httpStatus, close);
    }

    public static void servletResponse(HttpServletResponse response, String responseString, HttpStatus httpStatus, boolean close) throws IOException {
        response.setContentType(Constant.API_CONTENT_TYPE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding(Constant.DEFAULT_ENCODING);
        try {
            var writer = response.getWriter();
            writer.write(responseString);
            if (close) {
                writer.close();
            }
        } catch (IllegalStateException e) {
            log.error("IllegalStateException ERROR: {}", e.getMessage());
            log.error("IllegalStateException ERROR [responseString]: {}", responseString);
            e.printStackTrace();
        }
    }

    private static HttpStatus mapHttpStatus(int code) {
        try {
            return HttpStatus.valueOf(code);
        } catch (Exception ex) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
