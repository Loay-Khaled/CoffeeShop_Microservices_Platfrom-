package com.coffeeshop.loyaltyservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentProcessedEvent {
    private Long orderId;
    private String customerUsername;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private LocalDateTime processedAt;

    public PaymentProcessedEvent() {}

    public PaymentProcessedEvent(Long orderId, String customerUsername, BigDecimal amount, 
                                  String paymentMethod, String status, LocalDateTime processedAt) {
        this.orderId = orderId;
        this.customerUsername = customerUsername;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.processedAt = processedAt;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
