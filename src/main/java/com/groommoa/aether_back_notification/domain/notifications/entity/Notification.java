package com.groommoa.aether_back_notification.domain.notifications.entity;

import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
@Builder
@Getter
public class Notification {

    @Id
    private ObjectId id;

    private ObjectId project;

    private String message;

    @Builder.Default
    private ObjectId sender = null;

    private ObjectId receiver;

    private NoticeType noticeType;

    private RelatedContent relatedContent;

    @Builder.Default
    private boolean isRead = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
