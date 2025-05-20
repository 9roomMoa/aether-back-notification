package com.groommoa.aether_back_notification.domain.notifications.service;

import com.groommoa.aether_back_notification.domain.notifications.dto.*;
import com.groommoa.aether_back_notification.domain.notifications.entity.Notification;
import com.groommoa.aether_back_notification.domain.notifications.entity.RelatedContent;
import com.groommoa.aether_back_notification.domain.notifications.exception.NotificationException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.groommoa.aether_back_notification.global.common.exception.ErrorCode.NOTIFICATION_ACCESS_DENIED;
import static com.groommoa.aether_back_notification.global.common.exception.ErrorCode.NOTIFICATION_NOT_FOUNT;

@Slf4j
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
                        .projectTitle(request.getRelatedContent().getProjectTitle())
                        .taskTitle(request.getRelatedContent().getTaskTitle())
                        .build())
                .isRead(request.isRead())
                .build();

        return notificationRepository.save(notification);
    }

    @Transactional
    public ReadNotificationResponseDto markNotificationsAsRead(String userId, ReadNotificationsRequestDto request){
        List<String> updatedIds = new ArrayList<>();
        List<String> alreadyReadIds = new ArrayList<>();
        List<String> notFoundIds = new ArrayList<>();
        List<String> accessDeniedIds = new ArrayList<>();

        List<String> notificationIds = request.getNotificationIds();
        for (String id : notificationIds) {
            Optional<Notification> optionalNotification = notificationRepository.findById(id);
            if (optionalNotification.isEmpty()){
                notFoundIds.add(id);
                continue;
            }
            Notification notification = optionalNotification.get();

            // 읽음 요청한 유저 id와 대상 알림의 수신자(receiver) id가 서로 일치하는지 검사
            if (!notification.getReceiver().toHexString().equals(userId)) {
                accessDeniedIds.add(id);
                continue;
            }

            if (!notification.isRead()){
                notification.setRead(true);
                notificationRepository.save(notification);

                updatedIds.add(id);
            } else {
                alreadyReadIds.add(id);
            }
        }
        return new ReadNotificationResponseDto(updatedIds, alreadyReadIds, notFoundIds, accessDeniedIds);
    }

    public void deleteNotification(String userId, String notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isEmpty()){
            throw new NotificationException(NOTIFICATION_NOT_FOUNT);
        }

        Notification notification = optional.get();
        if (!notification.getReceiver().toHexString().equals(userId)){
            throw new NotificationException(NOTIFICATION_ACCESS_DENIED);
        }
        notificationRepository.delete(notification);
    }
}
