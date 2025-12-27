package com.coffeeshop.paymentservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentProcessedEvent {
    public Long orderId;
    public String customerUsername;
    public BigDecimal amount;
    public String status; // SUCCESS, FAILED
    public LocalDateTime processedAt;
}
