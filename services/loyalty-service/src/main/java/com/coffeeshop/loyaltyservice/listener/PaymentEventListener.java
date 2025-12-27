package com.coffeeshop.loyaltyservice.listener;

import com.coffeeshop.loyaltyservice.event.PaymentProcessedEvent;
import com.coffeeshop.loyaltyservice.service.LoyaltyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PaymentEventListener.class);
    
    private final LoyaltyService loyaltyService;
    private final ObjectMapper objectMapper;

    public PaymentEventListener(LoyaltyService loyaltyService, ObjectMapper objectMapper) {
        this.loyaltyService = loyaltyService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payments.processed", groupId = "loyalty-service-group")
    public void handlePaymentProcessed(String message) {
        try {
            PaymentProcessedEvent event = objectMapper.readValue(message, PaymentProcessedEvent.class);
            
            if ("COMPLETED".equals(event.getStatus()) || "SUCCESS".equals(event.getStatus())) {
                logger.info("Processing payment event for order {} by user {}", 
                        event.getOrderId(), event.getCustomerUsername());
                
                loyaltyService.awardPoints(
                        event.getCustomerUsername(),
                        event.getOrderId(),
                        event.getAmount()
                );
            } else {
                logger.info("Skipping points award for non-successful payment: {}", event.getStatus());
            }
        } catch (Exception e) {
            logger.error("Error processing payment event: {}", e.getMessage(), e);
        }
    }
}
