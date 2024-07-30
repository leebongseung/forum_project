package com.example.forum_project.domain.member.service;

import com.example.forum_project.domain.member.dto.SignUpMemberReqDto;
import com.example.forum_project.domain.member.repository.MemberRepository;
import com.example.forum_project.domain.member.vo.ResponseMemberVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(MemberServiceImplTest.class);

    @Mock
    private MemberRepository repository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("[성공] 이메일,아이디 중복 없이 회원가입")
    void signUp() {
        // given
        SignUpMemberReqDto signUpMemberReqDto = SignUpMemberReqDto.of("loginId@123", "newPassword", "email.com", "010-0000-0000", "name");

        // when
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        ResponseMemberVo memberVo = memberService.signUp(signUpMemberReqDto);

        // then : member의 id가 제대로 생성되었는지 확인.
        log.info("회원가입 결과: {}", memberVo.toString());


        Assertions.assertThat(memberVo.getMemberId()).isNotNull();
        Assertions.assertThat(memberVo.getName()).isEqualTo("name");
        Assertions.assertThat(memberVo.getEmail()).isEqualTo("email.com");
        Assertions.assertThat(memberVo.getLoginId()).isEqualTo("loginId@123");
    }
}