package com.example.forum.domain.forum.vo;

import com.example.forum.domain.forum.entity.Forum;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record ResponseForum(
        String forumId, // 게시판
        String authorName, // 작성자
        String title, // 제목
        String content, // 내용
        Long views, // 조회수
        Long likes, // 좋아요 수
        LocalDateTime createAt, // 생성일
        LocalDateTime updateAt // 수정일
) {
    @QueryProjection // Q파일로 생성하는 방법
    public ResponseForum(Forum forum) {
        this(
                forum.getForumId(),
                forum.getAuthor().getName(),
                forum.getTitle(),
                forum.getContent(),
                forum.getViews(),
                forum.getLikes(),
                forum.getCreateAt(),
                forum.getUpdateAt()
        );
    }
}
