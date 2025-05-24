package com.groommoa.aether_back_notification.domain.notices.exception;

import com.groommoa.aether_back_notification.global.common.exception.CustomException;
import com.groommoa.aether_back_notification.global.common.exception.ErrorCode;

public class NoticeException extends CustomException {
    public NoticeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
