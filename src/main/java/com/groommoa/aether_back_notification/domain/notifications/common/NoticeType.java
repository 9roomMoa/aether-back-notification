package com.groommoa.aether_back_notification.domain.notifications.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {
    TASK_ASSIGNED("task_assigned"),
    TASK_UPDATED("task_updated"),
    TASK_DEADLINE("task_deadline"),
    COMMENT_ADDED("comment_added"),
    COMMENT_UPDATED("comment_updated"),
    DOCUMENT_UPLOADED("document_uploaded"),
    PROJECT_ADDED("project_added"),
    PROJECT_UPDATED("project_updated"),;

    private final String key;

    @JsonCreator
    public static NoticeType from(String value){
        for (NoticeType type: NoticeType.values()) {
            if (type.key.equalsIgnoreCase(value)){
                return type;
            }
        }
        throw new IllegalArgumentException(value);
    }

    @JsonValue
    public String toValue(){
        return key;
    }
}
