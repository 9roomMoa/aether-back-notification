package com.groommoa.aether_back_notification.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CommonResponse {

    private final int code;
    private final String message;
    private final Map<String, Object> result;
}
