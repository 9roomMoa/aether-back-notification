package com.groommoa.aether_back_notification.domain.notifications.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReadNotificationResponseDto {

    private final List<String> updatedIds;
    private final List<String> alreadyReadIds;
    private final List<String> notFoundIds;
    private final List<String> accessDeniedIds;
}
