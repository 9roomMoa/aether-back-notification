package com.groommoa.aether_back_notification.global.config;

import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new StringToNoticeTypeConverter(),
                new NoticeTypeToStringConverter()
        ));
    }

    static class StringToNoticeTypeConverter implements Converter<String, NoticeType> {
        @Override
        public NoticeType convert(String source) {
            for (NoticeType type : NoticeType.values()) {
                if (type.getKey().equalsIgnoreCase(source)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown NoticeType: " + source);
        }
    }

    static class NoticeTypeToStringConverter implements Converter<NoticeType, String> {
        @Override
        public String convert(NoticeType source) {
            return source.getKey();
        }
    }
}

