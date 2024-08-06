package com.example.forum.domain.member.service;

import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.vo.ResponseMemberVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

    // 회원가입
    ResponseMemberVo signUp(SignUpMemberReqDto signUpMemberReqDto);
    ResponseMemberVo getMemberByLoginId(String loginId);
}
