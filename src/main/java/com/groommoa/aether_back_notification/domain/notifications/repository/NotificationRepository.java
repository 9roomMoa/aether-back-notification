package com.groommoa.aether_back_notification.domain.notifications.repository;

import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Page<Notification> findByReceiverOrderByCreatedAtDesc(ObjectId receiverId, Pageable pageable);
}
