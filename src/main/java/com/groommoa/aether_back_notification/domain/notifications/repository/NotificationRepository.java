package com.groommoa.aether_back_notification.domain.notifications.repository;

import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
