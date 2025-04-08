package com.groommoa.aether_back_notification.domain.notifications.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedContent {

    private String id;
    private String type;
}
