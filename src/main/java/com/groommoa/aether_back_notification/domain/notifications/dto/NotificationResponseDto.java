package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponseDto {

    private final String id;
    private final String projectTitle;
    private final String taskTitle;
    private final NoticeType noticeType;
    private final String message;
    @JsonProperty("isRead")
    private final boolean read;
    private final String createdAt;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId().toHexString())
                .projectTitle(notification.getRelatedContent().getProjectTitle())
                .taskTitle(notification.getRelatedContent().getTaskTitle())
                .noticeType(notification.getNoticeType())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }
}
