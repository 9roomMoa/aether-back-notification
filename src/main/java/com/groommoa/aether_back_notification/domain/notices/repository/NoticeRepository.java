package com.groommoa.aether_back_notification.domain.notices.repository;

import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NoticeRepository extends MongoRepository<Notice, String> {
    List<Notice> findByExpiredAtAfterOrderByCreatedAtDesc(Instant now, Pageable pageable);

}
