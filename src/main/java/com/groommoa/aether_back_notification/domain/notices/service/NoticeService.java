package com.groommoa.aether_back_notification.domain.notices.service;

import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import com.groommoa.aether_back_notification.domain.notices.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice createNotice(String userId, CreateNoticeRequestDto request) {
        Notice notice = Notice.builder()
                .content(request.getContent())
                .createdBy(new ObjectId(userId))
                .build();

        return noticeRepository.save(notice);
    }
}
