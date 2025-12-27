package com.coffeeshop.notificationservice.listener;

import com.coffeeshop.notificationservice.event.OrderCreatedEvent;
import com.coffeeshop.notificationservice.model.Notification;
import com.coffeeshop.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final NotificationService notificationService;

    public OrderEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "orders.created", groupId = "notification-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Notify customer
        Notification customerNotification = new Notification(
            "ORDER_CREATED",
            "Order Received",
            "Your order #" + event.getOrderId() + " has been received and is being prepared!",
            event.getCustomerUsername()
        );
        customerNotification.setOrderId(String.valueOf(event.getOrderId()));
        notificationService.sendNotification(customerNotification);

        // Notify barista queue
        Notification baristaNotification = new Notification(
            "ORDER_CREATED",
            "New Order",
            "Order #" + event.getOrderId() + " for " + event.getCustomerUsername(),
            "barista"
        );
        baristaNotification.setOrderId(String.valueOf(event.getOrderId()));
        notificationService.sendNotification(baristaNotification);

        System.out.println("Processed order created event for order: " + event.getOrderId());
    }
}
