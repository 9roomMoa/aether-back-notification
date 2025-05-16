package com.groommoa.aether_back_notification.domain.notifications.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RelatedContentType {
    Task("TASK"),
    Comment("COMMENT"),
    Document("DOCUMENT"),
    Project("PROJECT");

    private final String key;
}
