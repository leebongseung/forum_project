package com.example.forum.domain.forum.service;

import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.forum.repository.ForumRepository;
import com.example.forum.domain.forum.vo.ResponseForum;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForumServiceImpl implements ForumService {

    private final MemberService memberService;
    private final ForumRepository repository;

    @Override
    public ResponseForum createForum(
            ForumReqDto forumReqDto,
            String memberId
    ) {
        Member member = memberService.getMemberByMemberId(memberId);
        Forum forum = Forum.of(forumReqDto, member);
        repository.save(forum);
        return new ResponseForum(forum);
    }

    @Override
    public ResponseForum updateForum(ForumReqDto forumReqDto, String memberId) {
        return null;
    }

    @Override
    public ResponseForum deleteForum(Long memberId) {
        return null;
    }
}
