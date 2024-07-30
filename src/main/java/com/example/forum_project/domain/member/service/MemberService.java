package com.example.forum_project.domain.member.service;

import com.example.forum_project.domain.member.dto.SignUpMemberReqDto;
import com.example.forum_project.domain.member.vo.ResponseMemberVo;

public interface MemberService {
    // 회원가입
    public ResponseMemberVo signUp(SignUpMemberReqDto signUpMemberReqDto);
}
