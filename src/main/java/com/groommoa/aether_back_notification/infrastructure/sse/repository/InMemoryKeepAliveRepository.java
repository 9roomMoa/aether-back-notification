package com.groommoa.aether_back_notification.infrastructure.sse.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class InMemoryKeepAliveRepository implements KeepAliveRepository {

    private final Map<String, ScheduledExecutorService> keepAliveExecuters = new ConcurrentHashMap<String, ScheduledExecutorService>();

    private final long KEEP_ALIVE_INITIAL_DELAY = 0L;
    private final long KEEP_ALIVE_INTERVAL = 30L;
    private final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;

    @Override
    public void start(String userId, Runnable task) {
        if (keepAliveExecuters.containsKey(userId)) {
            log.info("keep-alive 서비스가 이미 실행 중입니다. (userId={})", userId);
            return;
        }

        ScheduledExecutorService executer = Executors.newSingleThreadScheduledExecutor();
        keepAliveExecuters.put(userId, executer);

        executer.scheduleAtFixedRate(task, KEEP_ALIVE_INITIAL_DELAY, KEEP_ALIVE_INTERVAL, KEEP_ALIVE_UNIT);

        log.info("keep-alive 서비스가 시작되었습니다. (userId={})", userId);
    }

    @Override
    public void stop(String userId) {
        ScheduledExecutorService executer = keepAliveExecuters.remove(userId);
        if (executer != null) {
            executer.shutdownNow();
            log.info("keep-alive 서비스가 중지되었습니다. (userId={})", userId);
        }
    }

    @Override
    public boolean isRunning(String userId) {
        return keepAliveExecuters.containsKey(userId);
    }

    @Override
    public void clearAll() {
        keepAliveExecuters.values().forEach(ScheduledExecutorService::shutdownNow);
        keepAliveExecuters.clear();
        log.info("모든 keep-alive 서비스가 삭제되었습니다.");
    }
}
