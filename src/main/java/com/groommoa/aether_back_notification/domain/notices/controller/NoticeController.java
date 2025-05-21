package com.groommoa.aether_back_notification.domain.notices.controller;

import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeResponseDto;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import com.groommoa.aether_back_notification.domain.notices.service.NoticeService;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.CommonResponse;
import com.groommoa.aether_back_notification.global.common.utils.DtoUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notices")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> createNotice(
            @AuthenticationPrincipal Claims claims,
            @RequestBody CreateNoticeRequestDto request
            ) {
        String userId = claims.getSubject();
        Notice notice = noticeService.createNotice(userId, request);
        CreateNoticeResponseDto responseDto = new CreateNoticeResponseDto(notice);

        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "공지 생성에 성공했습니다.", DtoUtils.toMap(responseDto));

        return ResponseEntity.ok(response);
    }

}
