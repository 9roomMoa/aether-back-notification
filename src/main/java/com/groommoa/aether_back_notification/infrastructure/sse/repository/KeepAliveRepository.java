package com.groommoa.aether_back_notification.infrastructure.sse.repository;

public interface KeepAliveRepository {
    void start(String userId, Runnable task);
    void stop(String userId);
    boolean isRunning(String userId);
    void clearAll();
}
