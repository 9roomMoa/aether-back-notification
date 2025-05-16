package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CreateNotificationResponseDto {

    private final String message;
    private final String sender;
    private final String receiver;
    private final NoticeType noticeType;
    private final RelatedContentDto relatedContent;
    private final boolean isRead;
    private final Instant createdAt;
    private final Instant updatedAt;

    public CreateNotificationResponseDto(Notification notification) {
        this.message = notification.getMessage();
        this.sender = notification.getSender().toHexString();
        this.receiver = notification.getReceiver().toHexString();
        this.noticeType = notification.getNoticeType();
        this.relatedContent = RelatedContentDto.builder()
                .id(notification.getRelatedContent().getId())
                .type(notification.getRelatedContent().getType())
                .projectTitle(notification.getRelatedContent().getProjectTitle())
                .taskTitle(notification.getRelatedContent().getTaskTitle())
                .build();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
        this.updatedAt = notification.getUpdatedAt();
    }
}
