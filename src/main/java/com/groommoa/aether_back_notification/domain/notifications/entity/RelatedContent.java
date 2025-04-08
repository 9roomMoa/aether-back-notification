package com.groommoa.aether_back_notification.domain.notifications.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedContent {

    @NotBlank(message = "relatedContent의 id는 null 값이거나 공백일 수 없습니다.")
    private String id;

    @NotBlank(message = "relatedContent의 type은 null 값이거나 공백일 수 없습니다.")
    private String type;
}
