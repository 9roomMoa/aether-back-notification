package com.groommoa.aether_back_notification.domain.notices.dto;

import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import lombok.Getter;

@Getter
public class CreateNoticeResponseDto {

    private final String content;
    private final String createdBy;
    private final String createdAt;

    public CreateNoticeResponseDto(Notice notice) {
        this.content = notice.getContent();
        this.createdBy = notice.getCreatedBy().toHexString();
        this.createdAt = notice.getCreatedAt().toString();
    }
}
