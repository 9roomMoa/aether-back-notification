package com.groommoa.aether_back_notification.domain.notifications.controller;

import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationRequestDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.NotificationPageResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.NotificationResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.service.NotificationService;
import com.groommoa.aether_back_notification.infrastructure.sse.service.SseService;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.CommonResponse;
import com.groommoa.aether_back_notification.global.common.utils.DtoUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequestMapping("/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {


    private final NotificationService notificationService;
    private final SseService sseService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> createNotification(@RequestBody @Valid CreateNotificationRequestDto request){
        Notification notification = notificationService.createNotification(request);
        CreateNotificationResponseDto responseDto = new CreateNotificationResponseDto(notification);


        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "알림 생성에 성공했습니다.", DtoUtils.toMap(responseDto));

        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param claims    JWT 토큰에서 추출한 인증 정보
     * @param page      요청할 페이지 번호 (0부터 시작)
     * @param size      한 패아자애 포함할 알림 개수
     * @return 알림 목록 및 페이지네이션 메타데이터
     */
    @GetMapping("")
    public ResponseEntity<CommonResponse> getNotifications(
            @AuthenticationPrincipal Claims claims,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        NotificationPageResponseDto responseDto = notificationService.getNotifications(claims.getSubject(), page, size);
        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "알림 상세 조회에 성공했습니다.", DtoUtils.toMap(responseDto));

        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/subscribe", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal Claims claims){
        return sseService.subscribe(claims.getSubject());
    }
}
