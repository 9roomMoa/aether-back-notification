package com.groommoa.aether_back_notification.domain.notifications.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NotificationPageResponseDto {
    private final List<NotificationResponseDto> notifications;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNext;
}
