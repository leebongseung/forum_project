package com.example.forum.domain.member.service;

import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.vo.ResponseMember;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

    // 회원가입
    ResponseMember signUp(SignUpMemberReqDto signUpMemberReqDto);
    ResponseMember getMemberVoByLoginId(String loginId);
    Member getMemberByLoginId(String loginId);
}
