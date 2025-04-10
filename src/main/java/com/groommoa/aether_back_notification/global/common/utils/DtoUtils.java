package com.groommoa.aether_back_notification.global.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class DtoUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> toMap(Object dto){
        return objectMapper.convertValue(dto, new TypeReference<>() {});
    }
}
