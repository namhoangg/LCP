package com.lcp.payment.mapper;

import com.lcp.payment.dto.PaymentCreateDto;
import com.lcp.payment.dto.PaymentResponseDto;
import com.lcp.payment.entity.Payment;
import com.lcp.util.MapUtil;
import com.lcp.util.TimeZoneUtil;

public class PaymentMapper {
    public static Payment createEntity(PaymentCreateDto createDto) {
        Payment payment = new Payment();
        MapUtil.copyProperties(createDto, payment);
        return payment;
    }

    public static PaymentResponseDto createResponse(Payment entity) {
        PaymentResponseDto responseDto = new PaymentResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        responseDto.setPaymentDate(TimeZoneUtil.convertToVietnamTime(entity.getPaymentDate()));
        return responseDto;
    }
}
