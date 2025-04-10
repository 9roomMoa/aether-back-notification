package com.groommoa.aether_back_notification.domain.notifications.controller;

import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationRequestDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.service.NotificationService;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.CommonResponse;
import com.groommoa.aether_back_notification.global.common.utils.DtoUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {


    private final NotificationService notificationService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> createNotification(@RequestBody @Valid CreateNotificationRequestDto request){
        Notification notification = notificationService.createNotification(request);
        CreateNotificationResponseDto responseDto = new CreateNotificationResponseDto(notification);


        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "알림 생성에 성공했습니다.", DtoUtils.toMap(responseDto));

        return ResponseEntity.ok(response);
    }
}
