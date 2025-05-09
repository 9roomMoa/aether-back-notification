package com.groommoa.aether_back_notification.infrastructure.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySseRepository implements SseRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<String, SseEmitter>();


    @Override
    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    @Override
    public SseEmitter get(String userId) {
        return emitters.get(userId);
    }

    @Override
    public void delete(String userId) {
        emitters.remove(userId);
    }

    @Override
    public boolean exists(String userId) {
        return emitters.containsKey(userId);
    }
}
