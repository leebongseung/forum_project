package com.example.forum.domain.forum.repository;

import com.example.forum.domain.forum.entity.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long> {
}
