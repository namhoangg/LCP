package com.lcp.payment.helper;

import com.lcp.payment.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class PaymentHelper {
    private static final String PAYMENT_CODE_FORMAT = "%s%06d";
    private static final String PAYMENT_CODE_PREFIX = "PM";
    private final PaymentRepository paymentRepository;

    public String genPaymentCode() {
        long count = paymentRepository.count();
        return String.format(PAYMENT_CODE_FORMAT, PAYMENT_CODE_PREFIX, ++count);
    }
}
