package com.lcp.payment.repository;

import com.lcp.payment.entity.Payment;
import com.lcp.payment.repository.custom.PaymentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    Payment findByTransactionId(String transactionId);
}
