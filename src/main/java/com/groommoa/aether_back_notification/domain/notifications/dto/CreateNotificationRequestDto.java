package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.groommoa.aether_back_notification.domain.notifications.entity.NoticeType;
import com.groommoa.aether_back_notification.domain.notifications.entity.RelatedContent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CreateNotificationRequestDto {

    @NotBlank
    private String message;

    @NotNull
    private ObjectId sender;

    @NotNull
    private ObjectId receiver;

    @NotNull
    private NoticeType noticeType;

    @NotNull
    private RelatedContent relatedContent;

    private boolean isRead;

}
