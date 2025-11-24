package com.lcp.paypal.controller;

import com.lcp.paypal.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private final PayPalService payPalService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam BigDecimal amount) {
        Map<String, Object> response = payPalService.createOrder(amount);

        // Extract approval link
        String approvalLink = null;
        List<Map<String, String>> links = (List<Map<String, String>>) response.get("links");
        for (Map<String, String> link : links) {
            if ("approve".equals(link.get("rel"))) {
                approvalLink = link.get("href");
                break;
            }
        }

        return ResponseEntity.ok(Map.of(
                "orderId", response.get("id"),
                "approvalUrl", approvalLink
        ));
    }


    @GetMapping("/capture")
    public ResponseEntity<?> captureOrder(@RequestParam("token") String orderId) {
        Map<String, Object> result = payPalService.captureOrder(orderId);

        // TODO: Store payment, match to your invoice/order system
        // You can extract net_amount, paypal_fee, etc.

        return ResponseEntity.ok(Map.of(
                "status", "Captured",
                "paypal_response", result
        ));
    }
}
