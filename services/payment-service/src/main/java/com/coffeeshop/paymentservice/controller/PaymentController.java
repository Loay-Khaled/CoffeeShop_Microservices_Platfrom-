package com.coffeeshop.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/process/{orderId}")
    public ResponseEntity<Map<String, Object>> processPayment(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("status", "PROCESSED");
        response.put("message", "Payment processed manually");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{orderId}")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("status", "COMPLETED");
        response.put("amount", "100.00");
        return ResponseEntity.ok(response);
    }
}
