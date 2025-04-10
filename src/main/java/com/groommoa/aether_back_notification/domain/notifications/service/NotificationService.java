package com.groommoa.aether_back_notification.domain.notifications.service;

import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationRequestDto;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.entity.RelatedContent;
import com.groommoa.aether_back_notification.domain.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(CreateNotificationRequestDto request) {
        Notification notification = Notification.builder()
                .project(request.getProject())
                .message(request.getMessage())
                .sender(request.getSender())
                .receiver(request.getReceiver())
                .noticeType(request.getNoticeType())
                .relatedContent(RelatedContent.builder()
                        .id(request.getRelatedContent().getId())
                        .type(request.getRelatedContent().getType())
                        .build())
                .isRead(request.isRead())
                .build();
        return notificationRepository.save(notification);
    }
}
