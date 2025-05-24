package com.groommoa.aether_back_notification.domain.notices.service;

import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.dto.NoticeDto;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import com.groommoa.aether_back_notification.domain.notices.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

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

    public List<NoticeDto> getNotices(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Instant now = Instant.now();

        List<Notice> notices = noticeRepository.findByExpiredAtAfterOrderByCreatedAtDesc(now, pageable);

        return notices.stream()
                .map(NoticeDto::from)
                .toList();
    }
}
