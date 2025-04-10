package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.groommoa.aether_back_notification.domain.notifications.common.NoticeType;
import com.groommoa.aether_back_notification.global.common.validation.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotificationRequestDto {

    @NotNull(message = "project 필드는 null 값일 수 없습니다.")
    private String project;

    @NotBlank(message = "message 필드는 null 값이거나 공백일 수 없습니다.")
    private String message;

    private String sender;

    @NotNull(message = "receiver 필드는 null 값일 수 없습니다.")
    private String receiver;

    @ValidEnum(enumClass = NoticeType.class, message = "noticeType이 유효하지 않습니다.")
    @NotNull(message = "noticeType 필드는 null 값일 수 없습니다.")
    private NoticeType noticeType;

    @Valid
    @NotNull(message = "relatedContent 필드는 null 값일 수 없습니다.")
    private RelatedContentDto relatedContent;

    private boolean isRead;

}
