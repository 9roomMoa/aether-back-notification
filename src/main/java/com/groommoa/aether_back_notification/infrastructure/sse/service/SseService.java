package com.groommoa.aether_back_notification.infrastructure.sse.service;

import com.groommoa.aether_back_notification.infrastructure.sse.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final long SSE_TIMEOUT = 0L;    // 제한 없음
    private final long KEEP_ALIVE_INITIAL_DELAY = 0L;
    private final long KEEP_ALIVE_INTERVAL = 30L;
    private final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
    private final SseRepository sseRepository;

    public SseEmitter subscribe(String userId){
        if (sseRepository.exists(userId)) {
            sseRepository.delete(userId);
        }

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        sseRepository.save(userId, emitter);

        emitter.onCompletion(() -> sseRepository.delete(userId));
        emitter.onTimeout(() -> sseRepository.delete(userId));
        emitter.onError((e) -> sseRepository.delete(userId));

        sendEvent(userId, "connect", "SSE connected");
        startKeepAlive(userId);

        return emitter;
    }

    public void sendEvent(String userId, String eventName, Object data){
        SseEmitter emitter = sseRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e){
                log.warn("SSE 전송 실패: " + e.getMessage());
                sseRepository.delete(userId);
            }
        }

    }

    private void startKeepAlive(String userId){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            sendEvent(userId, "keep_alive", "ping");
        }, KEEP_ALIVE_INITIAL_DELAY, KEEP_ALIVE_INTERVAL, KEEP_ALIVE_UNIT);
    }
}
