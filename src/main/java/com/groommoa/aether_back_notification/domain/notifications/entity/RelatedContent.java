package com.groommoa.aether_back_notification.domain.notifications.entity;

import com.groommoa.aether_back_notification.domain.notifications.common.RelatedContentType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedContent {

    @Field("id")
    private String id;

    private RelatedContentType type;
}
