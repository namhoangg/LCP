package com.lcp.paypal.service;

import java.math.BigDecimal;
import java.util.Map;

public interface PayPalService {
    Map<String, Object> createOrder(BigDecimal amount);
    Map<String, Object> captureOrder(String orderId);
}
