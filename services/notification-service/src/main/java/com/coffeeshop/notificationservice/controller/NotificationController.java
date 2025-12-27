package com.coffeeshop.notificationservice.controller;

import com.coffeeshop.notificationservice.model.Notification;
import com.coffeeshop.notificationservice.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getNotifications(@RequestParam String username) {
        return notificationService.getUserNotifications(username);
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeUser(@RequestParam String username) {
        return notificationService.subscribeUser(username);
    }

    @GetMapping(value = "/barista/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeBaristaQueue() {
        return notificationService.subscribeBaristaQueue();
    }
}
