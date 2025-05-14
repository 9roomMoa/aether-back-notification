package com.groommoa.aether_back_notification.domain.notifications.service;

import com.groommoa.aether_back_notification.domain.notifications.dto.CreateNotificationRequestDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.NotificationPageResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.dto.NotificationResponseDto;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.entity.RelatedContent;
import com.groommoa.aether_back_notification.domain.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationPageResponseDto getNotifications(String userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        ObjectId receiverId = new ObjectId(userId);
        Page<Notification> resultPage = notificationRepository.findByReceiverOrderByCreatedAtDesc(receiverId, pageable);

        List<NotificationResponseDto> content = resultPage.getContent().stream()
                .map(NotificationResponseDto::from)
                .toList();

        return NotificationPageResponseDto.builder()
                .notifications(content)
                .page(resultPage.getNumber())
                .size(resultPage.getSize())
                .totalPages(resultPage.getTotalPages())
                .totalElements(resultPage.getTotalElements())
                .hasNext(resultPage.hasNext())
                .build();
    }

    public List<String> getNotificationIds(String userId, String lastEventId){
        ObjectId receiverId = new ObjectId(userId);
        ObjectId lastId = new ObjectId(lastEventId);

        List<Notification> missedNotifications = notificationRepository
                .findByReceiverAndIdGreaterThanOrderByCreatedAtDesc(receiverId, lastId);

        return missedNotifications.stream()
                .map(n -> n.getId().toHexString())
                .toList();
    }


    @Transactional
    public Notification createNotification(CreateNotificationRequestDto request) {
        Notification notification = Notification.builder()
                .project(new ObjectId(request.getProject()))
                .message(request.getMessage())
                .sender(new ObjectId(request.getSender()))
                .receiver(new ObjectId(request.getReceiver()))
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
