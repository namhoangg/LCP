package com.lcp.common;

import com.lcp.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class BaseExceptionHandler {

    /**
     * Handles validation exceptions (e.g., @Valid failures).
     *
     * @param ex The MethodArgumentNotValidException thrown during validation.
     * @return A ResponseEntity containing error details.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Set the HTTP status to 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        String errMessage = ex.getBindingResult().getFieldError() != null ? ex.getBindingResult().getFieldError().getDefaultMessage() : Constant.VALIDATION_FAILED;

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errMessage,
                new ErrorDetail[]{}
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic exceptions (e.g., runtime errors).
     *
     * @param ex The Exception thrown.
     * @return A ResponseEntity containing error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericExceptions(Exception ex) {

        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage() != null ? ex.getMessage() : Constant.INTERNAL_SERVER_ERROR,
                new ErrorDetail[]{}
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiExceptions(ApiException ex) {
        log.error("An API error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getCode()));
    }
}