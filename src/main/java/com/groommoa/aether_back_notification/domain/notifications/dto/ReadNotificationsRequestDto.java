package com.groommoa.aether_back_notification.domain.notifications.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReadNotificationsRequestDto {

    private final List<String> notificationIds;
}
