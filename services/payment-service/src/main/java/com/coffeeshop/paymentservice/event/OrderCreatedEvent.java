package com.coffeeshop.paymentservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderCreatedEvent {
    public Long orderId;
    public String customerUsername;
    public BigDecimal total;
    public LocalDateTime createdAt;
}
