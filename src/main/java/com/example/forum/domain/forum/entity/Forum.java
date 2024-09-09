package com.example.forum.domain.forum.entity;

import com.example.forum.common.entity.BaseEntity;
import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import static com.example.forum.domain.forum.entity.ForumEntityConstants.CONTENT_MAX_LENGTH;
import static com.example.forum.domain.forum.entity.ForumEntityConstants.TITLE_MAX_LENGTH;

/**
 * 게시판 엔티티를 다음과 같이 설계하였다.
 * <p>
 * 게시판 id, 작성자 id, 게시글, 내용, 조회 수, 좋아요 수 를 통해서
 * 작성자 1명이 여러개의 게시글을 생성할 수 있도록 ManyToOne 관계 매핑
 *
 * @since 1.0
 */
@Entity
@Getter
public class Forum extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시판 id

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member author; // 작성자

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title; // 제목

    @Column(nullable = false, length = CONTENT_MAX_LENGTH)
    private String content; // 내용

    @Column(nullable = false)
    private Long views = 0L; // 조회수

    @Column(nullable = false)
    private Long likes = 0L; // 좋아요 수

    public static Forum of(ForumReqDto forumReqDto, Member author) {
        Forum forum = new Forum();
        forum.content = forumReqDto.getContent();
        forum.title = forumReqDto.getTitle();
        forum.author = author;
        return forum;
    }
}
