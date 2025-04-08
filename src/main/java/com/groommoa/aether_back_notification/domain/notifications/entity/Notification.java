package com.groommoa.aether_back_notification.domain.notifications.entity;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notifications")
@Getter
public class Notification {

    @Id
    private ObjectId id;

    private String message;

    private ObjectId sender;

    private ObjectId receiver;

    private NoticeType noticeType;

    private RelatedContent relatedContent;

    private boolean isRead;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder
    public Notification(String message, ObjectId sender, ObjectId receiver, NoticeType noticeType,
                        RelatedContent relatedContent, boolean isRead) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.noticeType = noticeType;
        this.relatedContent = relatedContent;
        this.isRead = isRead;
    }
}
