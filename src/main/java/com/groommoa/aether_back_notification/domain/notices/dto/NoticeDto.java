package com.groommoa.aether_back_notification.domain.notices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groommoa.aether_back_notification.domain.notices.common.NoticeLabel;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NoticeDto {

    private final String id;
    private final String content;
    private final NoticeLabel noticeLabel;
    @JsonProperty("isRecent")
    private final boolean recent;
    private final String createdAt;

    public static NoticeDto from(Notice notice) {
        return NoticeDto.builder()
                .id(notice.getId().toHexString())
                .content(notice.getContent())
                .noticeLabel(notice.getNoticeLabel())
                .recent(isRecent(notice.getCreatedAt()))
                .createdAt(notice.getCreatedAt().toString())
                .build();
    }

    private static boolean isRecent(Instant createdAt) {
        return createdAt.isAfter(Instant.now().minusSeconds(24 * 60 * 60)); // 1일
    }
}
