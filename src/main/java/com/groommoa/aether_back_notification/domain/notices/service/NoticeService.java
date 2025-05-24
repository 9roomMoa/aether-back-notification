package com.groommoa.aether_back_notification.domain.notices.service;

import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.dto.NoticeDto;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import com.groommoa.aether_back_notification.domain.notices.exception.NoticeException;
import com.groommoa.aether_back_notification.domain.notices.repository.NoticeRepository;
import com.groommoa.aether_back_notification.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public NoticeDto updateNotice(String requesterId, String noticeId, String content) {
        Notice notice = getValidatedNotice(requesterId, noticeId);

        notice.setContent(content);
        noticeRepository.save(notice);

        return NoticeDto.from(notice);
    }

    public void deleteNotice(String requesterId, String noticeId) {
        Notice notice = getValidatedNotice(requesterId, noticeId);
        noticeRepository.delete(notice);
    }

    private Notice getValidatedNotice(String requesterId, String noticeId) {
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        if (optionalNotice.isEmpty()) {
            throw new NoticeException(ErrorCode.NOTICE_NOT_FOUND);
        }
        Notice notice = optionalNotice.get();
        String createdBy = notice.getCreatedBy().toHexString();
        if (requesterId != null && !requesterId.equals(createdBy)) {
            throw new NoticeException(ErrorCode.NOTICE_ACCESS_DENIED);
        }
        return notice;
    }
}
