package com.coffeeshop.paymentservice.service;

import com.coffeeshop.paymentservice.event.OrderCreatedEvent;
import com.coffeeshop.paymentservice.event.PaymentProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PaymentService {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebClient webClient;
    private final Random random = new Random();
    
    public PaymentService(KafkaTemplate<String, Object> kafkaTemplate, WebClient.Builder webClientBuilder) {
        this.kafkaTemplate = kafkaTemplate;
        this.webClient = webClientBuilder.baseUrl("http://order-service:8084").build();
    }
    
    @KafkaListener(topics = "orders.created", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Processing payment for order: {}", event.orderId);
        
        // Simulate payment processing (90% success rate)
        boolean paymentSuccess = random.nextDouble() > 0.1;
        
        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent();
        paymentEvent.orderId = event.orderId;
        paymentEvent.customerUsername = event.customerUsername;
        paymentEvent.amount = event.total;
        paymentEvent.status = paymentSuccess ? "SUCCESS" : "FAILED";
        paymentEvent.processedAt = LocalDateTime.now();
        
        // Publish payment result
        kafkaTemplate.send("payments.processed", paymentEvent);
        
        // Update order status via REST call
        if (paymentSuccess) {
            try {
                webClient.put()
                    .uri("/api/orders/{id}/status", event.orderId)
                    .bodyValue("PAID")
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
                log.info("Updated order {} status to PAID", event.orderId);
            } catch (Exception e) {
                log.error("Failed to update order status", e);
            }
        }
        
        log.info("Payment processed for order {}: {}", event.orderId, paymentEvent.status);
    }
}
