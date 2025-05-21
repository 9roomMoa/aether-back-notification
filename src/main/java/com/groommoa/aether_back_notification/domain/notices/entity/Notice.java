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
import java.time.temporal.ChronoUnit;

@Document(collection = "notices")
@Getter
@Setter
@Builder
public class Notice {

    @Id
    private ObjectId id;

    @Builder.Default
    private NoticeLabel noticeLabel = NoticeLabel.valueOf("INTERNAL");

    private String content;

    private ObjectId createdBy;

    @Builder.Default
    private Instant expiredAt = Instant.now().plus(3, ChronoUnit.DAYS);

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
