package com.example.forum.domain.forum.repository;

import com.example.forum.domain.forum.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    // forumId로 forum 조회
    Optional<Forum> findByForumId(String forumId);
}
