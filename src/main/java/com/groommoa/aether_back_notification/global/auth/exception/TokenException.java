package com.groommoa.aether_back_notification.global.auth.exception;

import com.groommoa.aether_back_notification.global.common.exception.CustomException;
import com.groommoa.aether_back_notification.global.common.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
