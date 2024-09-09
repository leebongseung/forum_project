package com.example.forum.domain.member.service;

import com.example.forum.domain.member.dto.LoginMemberReqDto;
import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.vo.ResponseMember;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

    // 회원가입
    ResponseMember signUp(SignUpMemberReqDto signUpMemberReqDto);
    // 로그인
    ResponseMember login(HttpServletResponse response, LoginMemberReqDto loginMemberReqDto);
    Member getMemberByMemberId(String memberId);
}
