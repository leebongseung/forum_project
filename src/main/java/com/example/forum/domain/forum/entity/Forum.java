package com.example.forum.domain.forum.entity;

import com.example.forum.common.entity.BaseEntity;
import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

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
@SQLDelete(sql = "UPDATE forum SET is_Delete = true WHERE id = ?")
@SQLRestriction("is_Delete = false")
public class Forum extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시판 id

    @Column(nullable = false, unique = true)
    private String forumId; // 게시글 해쉬 아이디

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
        forum.forumId = UUID.randomUUID().toString();
        forum.content = forumReqDto.getContent();
        forum.title = forumReqDto.getTitle();
        forum.author = author;
        return forum;
    }

    // 커스텀 메서드를 통해 필요한 필드만 업데이트
    public void updateForum(ForumReqDto forumReqDto) {
        if (title != null && !title.isEmpty()) {
            this.title = forumReqDto.getTitle();
        }

        if (content != null && !content.isEmpty()) {
            this.content = forumReqDto.getContent();
        }
    }

    // 조회 수 증가
    public void incrementViewCount() {
        this.views++;
    }

    // 좋아요 증가
    public void incrementLikeCount() {
        this.likes++;
    }

    // 좋아요 취소
    public void decrementLikeCount() {
        this.likes--;
    }

    public void updateViews(Long views) {
        if (views != null && views >= 0) {
            this.views += views;
        }
    }
}
