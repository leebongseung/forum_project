package com.example.forum.domain.forum.controller;

import com.example.forum.common.annotation.ApiErrorCodeExamples;
import com.example.forum.common.error.ErrorCode;
import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.service.ForumService;
import com.example.forum.domain.forum.vo.ResponseForum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "forum", description = "Forum API")
@Slf4j
@RestController
@RequestMapping("/forums")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @PostMapping
    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    @ApiErrorCodeExamples({ErrorCode.MEMBER_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.SC_UNAUTHORIZED})
    public ResponseForum createForum(
            @Valid @RequestBody ForumReqDto forumReqDto,
            @AuthenticationPrincipal String memberId
    ){
        return (forumService.createForum(forumReqDto, memberId));
    }

    @PutMapping("/{forumId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiErrorCodeExamples({ErrorCode.FORUM_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.SC_UNAUTHORIZED})
    public ResponseForum updateForum(
            @PathVariable String forumId,
            @Valid @RequestBody ForumReqDto forumReqDto,
            @AuthenticationPrincipal String memberId
    ) {
        return forumService.updateForum(forumId, forumReqDto, memberId);
    }

    @DeleteMapping("/{forumId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiErrorCodeExamples({ErrorCode.FORUM_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.SC_UNAUTHORIZED})
    public String deleteForum(
            @PathVariable String forumId,
            @AuthenticationPrincipal String memberId
    ) {
        forumService.deleteForum(forumId, memberId);
        return "success";
    }

    @PostMapping("/{forumId}/views")
    @Operation(summary = "게시글 조회수 증가", description = "게시글 조회수를 증가합니다.")
    @ApiErrorCodeExamples({ErrorCode.FORUM_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.SC_UNAUTHORIZED})
    public String incrementViewCount(@PathVariable String forumId) {
        forumService.incrementViewCount(forumId);
        return "success";
    }

    @PostMapping("/{forumId}/likes")
    @Operation(summary = "게시글 좋아요 증가", description = "게시글 좋아요를 증가합니다.")
    @ApiErrorCodeExamples({ErrorCode.FORUM_NOT_FOUND, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.SC_UNAUTHORIZED})
    public String incrementLikeCount(
            @PathVariable String forumId,
            @AuthenticationPrincipal String memberId
    ) {
        // 게시글 좋아요 증가
        forumService.likeForum(forumId, memberId);
        return "success";
    }
}
