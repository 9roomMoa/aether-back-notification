package com.groommoa.aether_back_notification.infrastructure.sse.controller;

import com.groommoa.aether_back_notification.infrastructure.sse.service.SseService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequestMapping("/sse")
@RequiredArgsConstructor
@RestController
public class SseController {

    private final SseService sseService;

    @GetMapping(value="/subscribe", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal Claims claims,
            @RequestHeader(value="Last-Event-ID", required = false) String lastEventId
    ) {
        String userId = claims.getSubject();
        log.info("SSE 구독 요청 들어옴 (userId={})", userId);

        return sseService.subscribe(userId, lastEventId);
    }
}
