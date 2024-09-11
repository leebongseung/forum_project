package com.example.forum.domain.forum.service;

import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.forum.vo.ResponseForum;
import org.springframework.data.domain.Page;

public interface ForumService {
    // 게시글 상세 조회
    ResponseForum selectForum(String forumId);
    // 게시글 목록 조회
    Page<ResponseForum> selectForums(int page, int size);
    // 게시글 생성
    ResponseForum createForum(ForumReqDto forumReqDto, String memberId);
    // 게시글 수정
    ResponseForum updateForum(String forumId, ForumReqDto forumReqDto, String memberId);
    // 게시글 삭제
    void deleteForum(String forumId,String memberId);
    // 게시글 조회수 증가
    void incrementViewCount(String forumId);
    // 게시글 좋아요
    void likeForum(String forumId, String memberId);
    // 게시글Id로 게시글 조회
    Forum getForumByForumId(String forumId);
    // 특정 게시물 조회
    Page<ResponseForum> searchByCondition(int page, int size, String keyword, String condition);
}
