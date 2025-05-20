package com.groommoa.aether_back_notification.domain.notices.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeLabel {
    INTERNAL("INTERNAL");

    private final String key;
}
