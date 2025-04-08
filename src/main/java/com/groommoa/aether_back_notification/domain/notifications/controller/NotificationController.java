package com.groommoa.aether_back_notification.domain.notifications.controller;

import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationRequestDto;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.service.NotificationService;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> createNotification(@RequestBody @Valid CreateNotificationRequestDto request){
        Notification notification = notificationService.createNotification(request);

        Map<String, Object> result = new HashMap<>();
        result.put("message", notification.getMessage());
        result.put("sender", notification.getSender().toString());
        result.put("receiver", notification.getReceiver().toString());
        result.put("noticeType", notification.getNoticeType());
        result.put("relatedContent", notification.getRelatedContent());
        result.put("isRead", notification.isRead());
        result.put("createdAt", notification.getCreatedAt());
        result.put("updatedAt", notification.getUpdatedAt());

        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "알림 생성에 성공했습니다.", result
        );
        return ResponseEntity.ok(response);
    }
}
