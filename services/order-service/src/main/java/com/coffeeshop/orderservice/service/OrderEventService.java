package com.coffeeshop.orderservice.service;

import com.coffeeshop.orderservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventService {
    
    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public OrderEventService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Publishing OrderCreated event for order: {}", event.orderId);
        kafkaTemplate.send("orders.created", event);
    }
}
