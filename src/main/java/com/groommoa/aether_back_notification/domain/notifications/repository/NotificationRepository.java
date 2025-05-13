package com.groommoa.aether_back_notification.domain.notifications.repository;

import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByReceiverAndIdGreaterThanOrderByCreatedAtDesc(ObjectId receiver, ObjectId lastEventId);

    Page<Notification> findByReceiverOrderByCreatedAtDesc(ObjectId receiverId, Pageable pageable);
}
