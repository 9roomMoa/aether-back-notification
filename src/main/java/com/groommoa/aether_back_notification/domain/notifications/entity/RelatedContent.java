package com.groommoa.aether_back_notification.domain.notifications.entity;

import com.groommoa.aether_back_notification.domain.notifications.common.RelatedContentType;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedContent {

    private ObjectId id;

    private RelatedContentType type;
}
