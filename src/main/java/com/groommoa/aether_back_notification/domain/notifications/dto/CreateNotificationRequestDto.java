package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.groommoa.aether_back_notification.domain.notifications.entity.NoticeType;
import com.groommoa.aether_back_notification.domain.notifications.entity.RelatedContent;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CreateNotificationRequestDto {

    @NotNull(message = "project 필드는 null 값일 수 없습니다.")
    private ObjectId project;

    @NotBlank(message = "message 필드는 null 값이거나 공백일 수 없습니다.")
    private String message;

    private ObjectId sender;

    @NotNull(message = "receiver 필드는 null 값일 수 없습니다.")
    private ObjectId receiver;

    @NotNull(message = "noticeType 필드는 null 값일 수 없습니다.")
    private NoticeType noticeType;

    @Valid
    @NotNull(message = "relatedContent 필드는 null 값일 수 없습니다.")
    private RelatedContent relatedContent;

    private boolean isRead;

}
