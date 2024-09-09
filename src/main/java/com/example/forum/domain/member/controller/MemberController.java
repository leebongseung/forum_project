package com.example.forum.domain.member.controller;

import com.example.forum.common.annotation.ApiErrorCodeExamples;
import com.example.forum.domain.member.dto.LoginMemberReqDto;
import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.service.MemberService;
import com.example.forum.domain.member.vo.ResponseMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.forum.common.error.ErrorCode.*;

@Tag(name = "member", description = "Member API")
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    @Operation(summary = "회원 가입", description = "회원가입을 요청합니다.")
    @ApiErrorCodeExamples({EMAIL_DUPLICATION, LOGIN_ID_DUPLICATION})
    public ResponseMember signUp(
            @Valid @RequestBody SignUpMemberReqDto signUpMemberReqDto
    ) {
        return memberService.signUp(signUpMemberReqDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 요청합니다.")
    @ApiErrorCodeExamples({MEMBER_NOT_FOUND, SC_UNAUTHORIZED})
    public ResponseMember login(
            HttpServletResponse response,
            @Valid @RequestBody LoginMemberReqDto loginMemberReqDto
    ) {
        return memberService.login(response, loginMemberReqDto);
    }
}
