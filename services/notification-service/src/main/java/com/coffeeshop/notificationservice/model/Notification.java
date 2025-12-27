package com.coffeeshop.notificationservice.model;

import java.time.LocalDateTime;

public class Notification {
    private String id;
    private String type;
    private String title;
    private String message;
    private String targetUser;
    private String orderId;
    private LocalDateTime createdAt;
    private boolean read;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }

    public Notification(String type, String title, String message, String targetUser) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.type = type;
        this.title = title;
        this.message = message;
        this.targetUser = targetUser;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTargetUser() { return targetUser; }
    public void setTargetUser(String targetUser) { this.targetUser = targetUser; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
