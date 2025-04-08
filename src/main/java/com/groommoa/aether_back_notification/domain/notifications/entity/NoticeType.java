package com.groommoa.aether_back_notification.domain.notifications.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {
    TASK_ASSIGNED("task_assigned"),
    TASK_UPDATED("task_updated"),
    TASK_DEADLINE("task_deadline"),
    COMMENT_ADDED("comment_added"),
    DOCUMENT_UPLOADED("document_uploaded"),
    PROJECT_ADDED("project_added");

    private final String key;
}
