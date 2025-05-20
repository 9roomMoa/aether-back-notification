package com.groommoa.aether_back_notification.domain.notifications.exception;

import com.groommoa.aether_back_notification.global.common.exception.CustomException;
import com.groommoa.aether_back_notification.global.common.exception.ErrorCode;

public class NotificationException extends CustomException {
    public NotificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
