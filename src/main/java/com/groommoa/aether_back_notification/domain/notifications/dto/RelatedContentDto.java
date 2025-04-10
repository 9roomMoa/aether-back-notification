package com.groommoa.aether_back_notification.domain.notifications.dto;

import com.groommoa.aether_back_notification.domain.notifications.common.RelatedContentType;
import com.groommoa.aether_back_notification.global.common.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedContentDto {

    @NotBlank(message = "relatedContent의 id는 null 값이거나 공백일 수 없습니다.")
    private String id;

    @ValidEnum(enumClass = RelatedContentType.class, message = "relatedContent의 type이 유효하지 않습니다.")
    @NotNull(message = "relatedContent의 type은 null 값일 수 없습니다.")
    private RelatedContentType type;
}
