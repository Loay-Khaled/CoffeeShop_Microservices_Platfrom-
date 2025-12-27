package com.coffeeshop.notificationservice.service;

import com.coffeeshop.notificationservice.model.Notification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    // Store notifications per user
    private final Map<String, List<Notification>> userNotifications = new ConcurrentHashMap<>();
    
    // Store SSE emitters for real-time updates
    private final Map<String, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    
    // Store barista queue emitters
    private final List<SseEmitter> baristaEmitters = new CopyOnWriteArrayList<>();

    public void sendNotification(Notification notification) {
        String user = notification.getTargetUser();
        
        // Store notification
        userNotifications.computeIfAbsent(user, k -> new CopyOnWriteArrayList<>()).add(notification);
        
        // Send via SSE to user
        sendSseToUser(user, notification);
        
        // If it's an order notification, also send to barista queue
        if ("ORDER_CREATED".equals(notification.getType()) || 
            "ORDER_READY".equals(notification.getType()) ||
            "ORDER_PROCESSING".equals(notification.getType())) {
            sendToBaristaQueue(notification);
        }
    }

    public List<Notification> getUserNotifications(String username) {
        return userNotifications.getOrDefault(username, Collections.emptyList());
    }

    public SseEmitter subscribeUser(String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        userEmitters.computeIfAbsent(username, k -> new CopyOnWriteArrayList<>()).add(emitter);
        
        emitter.onCompletion(() -> removeEmitter(username, emitter));
        emitter.onTimeout(() -> removeEmitter(username, emitter));
        emitter.onError(e -> removeEmitter(username, emitter));
        
        return emitter;
    }

    public SseEmitter subscribeBaristaQueue() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        baristaEmitters.add(emitter);
        
        emitter.onCompletion(() -> baristaEmitters.remove(emitter));
        emitter.onTimeout(() -> baristaEmitters.remove(emitter));
        emitter.onError(e -> baristaEmitters.remove(emitter));
        
        return emitter;
    }

    private void sendSseToUser(String username, Notification notification) {
        List<SseEmitter> emitters = userEmitters.get(username);
        if (emitters != null) {
            List<SseEmitter> deadEmitters = new ArrayList<>();
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(notification));
                } catch (IOException e) {
                    deadEmitters.add(emitter);
                }
            }
            emitters.removeAll(deadEmitters);
        }
    }

    private void sendToBaristaQueue(Notification notification) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : baristaEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("order-update")
                        .data(notification));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        baristaEmitters.removeAll(deadEmitters);
    }

    private void removeEmitter(String username, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(username);
        if (emitters != null) {
            emitters.remove(emitter);
        }
    }
}
