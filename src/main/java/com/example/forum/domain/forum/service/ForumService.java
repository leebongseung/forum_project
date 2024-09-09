package com.example.forum.domain.forum.service;

import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.vo.ResponseForum;

public interface ForumService {
    // 게시글 생성
    ResponseForum createForum(ForumReqDto forumReqDto, String memberId);
    // 게시글 수정
    ResponseForum updateForum(ForumReqDto forumReqDto, String memberId);
    // 게시글 삭제
    ResponseForum deleteForum(Long memberId);
}
