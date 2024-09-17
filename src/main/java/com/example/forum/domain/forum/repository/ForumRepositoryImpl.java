package com.example.forum.domain.forum.repository;

import com.example.forum.domain.forum.entity.QForum;
import com.example.forum.domain.forum.search.SearchType;
import com.example.forum.domain.forum.vo.QResponseForum;
import com.example.forum.domain.forum.vo.ResponseForum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ForumRepositoryImpl implements ForumRepositoryCustom {
    /* Querydsl 사용하기 위한 선언 */
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResponseForum> searchByCondition(
            SearchType keyword,
            String condition,
            Pageable pageable
    ) {
        QForum forum = QForum.forum;
        BooleanBuilder whereClause = new BooleanBuilder();

        switch (keyword) {
            case ALL -> {
                whereClause.or(forum.title.contains(condition));
                whereClause.or(forum.content.contains(condition));
                whereClause.or(forum.author.name.contains(condition));
            }
            case TITLE -> whereClause.or(forum.title.contains(condition));
            case CONTENT -> whereClause.or(forum.content.contains(condition));
            case AUTHOR -> whereClause.or(forum.author.name.contains(condition));
        }

        List<ResponseForum> fetch = queryFactory
                .select(new QResponseForum(forum))
                .from(forum)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Objects.requireNonNull(queryFactory
                .select(forum.count())
                .from(forum)
                .where(whereClause)
                .fetchOne());

        return new PageImpl<>(fetch, pageable, total);
    }
}
