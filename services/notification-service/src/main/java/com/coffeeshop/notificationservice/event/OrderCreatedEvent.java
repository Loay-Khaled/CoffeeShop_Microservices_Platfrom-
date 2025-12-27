package com.coffeeshop.notificationservice.event;

import java.math.BigDecimal;

public class OrderCreatedEvent {
    private Long orderId;
    private String customerUsername;
    private BigDecimal total;
    private String status;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
