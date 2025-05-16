package com.groommoa.aether_back_notification.domain.notifications.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReadNotificationsRequestDto {

    @NotEmpty(message="알림 ID 목록은 비어 있을 수 없습니다.")
    @Size(max=100, message="한번에 최대 100개의 알림한 처리할 수 있습니다.")
    private final List<String> notificationIds;
}
