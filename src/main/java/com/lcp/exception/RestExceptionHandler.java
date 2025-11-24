package com.lcp.exception;

import com.lcp.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler{
  @ExceptionHandler(ApiException.class)
  public void handleApiException(ApiException exception, HttpServletResponse response) throws IOException {
    log.error(exception.getMessage());
    Response.servletResponse(response, exception.getCode(), exception.getMessage());
  }
}
