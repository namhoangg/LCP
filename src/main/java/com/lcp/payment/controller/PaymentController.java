package com.lcp.payment.controller;

import com.lcp.common.Constant;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.common.enumeration.PaymentStatus;
import com.lcp.payment.common.ApiPaymentMessage;
import com.lcp.payment.dto.PaymentCreateDto;
import com.lcp.payment.dto.PaymentListRequest;
import com.lcp.payment.dto.PaymentResponseDto;
import com.lcp.payment.dto.PaymentUpdateDto;
import com.lcp.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(value = "")
    public Response<PaymentResponseDto> create(@Valid @RequestBody PaymentCreateDto createDto) {
        return Response.success(paymentService.create(createDto), ApiPaymentMessage.PAYMENT_CREATE_SUCCESS);
    }

    @GetMapping(value = "/capture", produces = Constant.API_CONTENT_TYPE)
    public Response<PaymentResponseDto> capture(@RequestParam("orderId") String orderId) {
        return Response.success(paymentService.capture(orderId), ApiPaymentMessage.PAYMENT_CAPTURE_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}/send-payment-request")
    public Response<PaymentResponseDto> sendPaymentRequest(@PathVariable Long id) {
        return Response.success(paymentService.sendPaymentRequest(id), ApiPaymentMessage.PAYMENT_SEND_PAYMENT_REQUEST_SUCCESS);
    }

    @PutMapping(value = "/{id:-?\\d+}/update-payment-request")
    public Response<PaymentResponseDto> updatePaymentRequest(@PathVariable Long id, @RequestBody PaymentUpdateDto updateDto) {
        return Response.success(paymentService.updatePaymentRequest(id, updateDto), ApiPaymentMessage.PAYMENT_STATUS_UPDATE_SUCCESS);
    }

    @GetMapping(value = "/{id:-?\\d+}")
    public Response<PaymentResponseDto> detail(@PathVariable Long id) {
        return Response.success(paymentService.detail(id));
    }

    @GetMapping(value = "")
    public Response<PageResponse<PaymentResponseDto>> list(PaymentListRequest request) {
        return Response.success(paymentService.list(request));
    }
}
