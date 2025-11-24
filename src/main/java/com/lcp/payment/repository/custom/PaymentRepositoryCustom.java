package com.lcp.payment.repository.custom;

import com.lcp.payment.dto.PaymentListRequest;
import com.lcp.payment.entity.Payment;
import org.springframework.data.domain.Page;

public interface PaymentRepositoryCustom {
    Page<Payment> list(PaymentListRequest request);
}
