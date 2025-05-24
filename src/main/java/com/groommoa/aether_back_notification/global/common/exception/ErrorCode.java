package com.groommoa.aether_back_notification.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "잘못된 registration id 입니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),

    // notification
    NOTIFICATION_NOT_FOUNT(NOT_FOUND, "요청한 알림을 찾을 수 없습니다."),
    NOTIFICATION_ACCESS_DENIED(FORBIDDEN, "요청한 알림에 접근할 권한이 없습니다."),

    // notice
    NOTICE_NOT_FOUND(NOT_FOUND, "요청한 공지를 찾을 수 없습니다."),
    NOTICE_ACCESS_DENIED(FORBIDDEN, "요청한 공지에 접근할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
