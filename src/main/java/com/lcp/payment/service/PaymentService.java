package com.lcp.payment.service;

import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.PaymentStatus;
import com.lcp.payment.dto.PaymentCreateDto;
import com.lcp.payment.dto.PaymentListRequest;
import com.lcp.payment.dto.PaymentResponseDto;
import com.lcp.payment.dto.PaymentUpdateDto;

public interface PaymentService {
    PaymentResponseDto create(PaymentCreateDto createDto);

    PaymentResponseDto capture(String orderId);

    PaymentResponseDto detail(Long id);

    PageResponse<PaymentResponseDto> list(PaymentListRequest request);

    PaymentResponseDto sendPaymentRequest(Long id);

    PaymentResponseDto updatePaymentRequest(Long id, PaymentUpdateDto updateDto);
}
