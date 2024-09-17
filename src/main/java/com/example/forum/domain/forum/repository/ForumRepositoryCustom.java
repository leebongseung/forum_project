package com.example.forum.domain.forum.repository;

import com.example.forum.domain.forum.search.SearchType;
import com.example.forum.domain.forum.vo.ResponseForum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ForumRepositoryCustom {
    // keyword를 바탕으로 condition 조회
    Page<ResponseForum> searchByCondition(SearchType keyword, String condition, Pageable pageable);
}
