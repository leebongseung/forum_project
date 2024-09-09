package com.example.forum.domain.member.vo;

import com.example.forum.domain.member.entity.Member;

public record ResponseMember(String memberId, String loginId, String email, String name) {
    public ResponseMember(Member member){
        this(member.getMemberId(), member.getLoginId(), member.getEmail(), member.getName());
    }
}
