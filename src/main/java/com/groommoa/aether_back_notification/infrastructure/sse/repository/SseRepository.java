package com.groommoa.aether_back_notification.infrastructure.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface SseRepository {
    void save(String userId, SseEmitter emitter);
    SseEmitter get(String userId);
    void delete(String userId);
    boolean exists(String userId);
    Map<String, SseEmitter> getAllEmitters();
}
