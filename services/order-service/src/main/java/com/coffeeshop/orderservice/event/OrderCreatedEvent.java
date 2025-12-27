package com.coffeeshop.orderservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderCreatedEvent {
    public Long orderId;
    public String customerUsername;
    public BigDecimal total;
    public LocalDateTime createdAt;
    public List<OrderItemDto> items;

    public static class OrderItemDto {
        public Long productId;
        public String productName;
        public BigDecimal unitPrice;
        public Integer quantity;
    }
}
