package com.example.forum_project.domain.member.controller;

import com.example.forum_project.common.annotation.ApiErrorCodeExamples;
import com.example.forum_project.common.error.ErrorCode;
import com.example.forum_project.domain.member.dto.SignUpMemberReqDto;
import com.example.forum_project.domain.member.service.MemberService;
import com.example.forum_project.domain.member.vo.ResponseMemberVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "Member API")
@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "회원 가입", description = "회원가입을 요청합니다.")
    @ApiErrorCodeExamples({ErrorCode.EMAIL_DUPLICATION, ErrorCode.LOGIN_ID_DUPLICATION})
    public ResponseMemberVo signUp(
            @Valid @RequestBody SignUpMemberReqDto signUpMemberReqDto
    ) {
        return memberService.signUp(signUpMemberReqDto);
    }
}
