package com.example.forum.domain.forum.repository;

import com.example.forum.domain.forum.entity.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    // forumId로 forum 조회
    Optional<Forum> findByForumId(String forumId);

    // 제목으로 목록 조회
    // todo : querydsl로 리팩터링
    Page<Forum> findByTitleContaining(String keyword, Pageable pageable);

    // 내용으로 목록 조회
    // todo : querydsl로 리팩터링
    Page<Forum> findByContentContaining(String keyword, Pageable pageable);

    // 작성자로 목록 조회
    // todo : querydsl로 리팩터링
    Page<Forum> findByAuthorNameContaining(String keyword, Pageable pageable);

    // 제목, 내용, 작성자로 목록조회
    // todo : querydsl로 리팩터링
    Page<Forum> findByTitleContainingOrContentContainingOrAuthorNameContaining(String keyword, String keyword1, String keyword2, Pageable pageable);
}
