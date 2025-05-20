package com.groommoa.aether_back_notification.domain.notices.entity;

import com.groommoa.aether_back_notification.domain.notices.common.NoticeLabel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notices")
@Getter
@Setter
@Builder
public class Notice {

    @Id
    private ObjectId id;

    private NoticeLabel noticeLabel;

    private String title;

    private String content;

    private ObjectId createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
