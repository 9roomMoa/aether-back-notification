package com.groommoa.aether_back_notification.domain.notices.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateNoticeRequestDto {
    private final String content;
}
