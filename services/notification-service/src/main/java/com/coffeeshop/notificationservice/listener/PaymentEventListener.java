package com.coffeeshop.notificationservice.listener;

import com.coffeeshop.notificationservice.event.PaymentProcessedEvent;
import com.coffeeshop.notificationservice.model.Notification;
import com.coffeeshop.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private final NotificationService notificationService;

    public PaymentEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "payments.processed", groupId = "notification-service")
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        String title, message;
        
        if ("SUCCESS".equals(event.getStatus())) {
            title = "Payment Successful";
            message = "Payment for order #" + event.getOrderId() + " was successful. Your order is being prepared!";
        } else {
            title = "Payment Failed";
            message = "Payment for order #" + event.getOrderId() + " failed. Please try again.";
        }

        Notification notification = new Notification(
            "PAYMENT_" + event.getStatus(),
            title,
            message,
            event.getCustomerUsername()
        );
        notification.setOrderId(String.valueOf(event.getOrderId()));
        notificationService.sendNotification(notification);

        System.out.println("Processed payment event for order: " + event.getOrderId() + " status: " + event.getStatus());
    }
}
