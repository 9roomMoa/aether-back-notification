package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponseDto {

    private final String projectId;
    private final String taskId;
    private final NoticeType noticeType;
    private final String message;
    @JsonProperty("isRead")
    private final boolean read;
    private final String createdAt;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .projectId(notification.getProject().toHexString())
                .taskId(notification.getRelatedContent().getId())
                .noticeType(notification.getNoticeType())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
