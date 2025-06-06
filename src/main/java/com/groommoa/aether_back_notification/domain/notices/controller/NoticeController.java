package com.groommoa.aether_back_notification.domain.notices.controller;

import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.dto.CreateNoticeResponseDto;
import com.groommoa.aether_back_notification.domain.notices.dto.NoticeDto;
import com.groommoa.aether_back_notification.domain.notices.dto.UpdateNoticeRequestDto;
import com.groommoa.aether_back_notification.domain.notices.entity.Notice;
import com.groommoa.aether_back_notification.domain.notices.service.NoticeService;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.CommonResponse;
import com.groommoa.aether_back_notification.global.common.utils.DtoUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("")
    public ResponseEntity<CommonResponse> getNotices(
            @RequestParam(defaultValue = "3") @Min(1) @Max(10) int limit
    ) {
        List<NoticeDto> content = noticeService.getNotices(limit);
        Map<String, Object> result = Map.of("notices", content);

        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "공지 조회에 성공했습니다.", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity<CommonResponse> updateNotice(
            @AuthenticationPrincipal Claims claims,
            @PathVariable String noticeId,
            @Valid @RequestBody UpdateNoticeRequestDto request
    ) {
        String requesterId = claims.getSubject();
        String content = request.getContent();
        NoticeDto updatedNotice = noticeService.updateNotice(requesterId, noticeId, content);

        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "공지 수정에 성공했습니다.", DtoUtils.toMap(updatedNotice));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<CommonResponse> deleteNotice(
            @AuthenticationPrincipal Claims claims,
            @PathVariable String noticeId
    ) {
        String requesterId = claims.getSubject();
        noticeService.deleteNotice(requesterId, noticeId);

        CommonResponse response = new CommonResponse(
                HttpStatus.OK, "공지 삭제에 성공했습니다.", null);

        return ResponseEntity.ok(response);
    }

}
