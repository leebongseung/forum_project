package com.example.forum.config.filter;

import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.config.jwt.JwtTokenGenerator;
import com.example.forum.config.security.CustomUserDetails;
import com.example.forum.domain.member.dto.LoginMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.forum.common.error.ErrorCode.INVALID_INPUT_VALUE;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final MemberService memberService;
    private final JwtTokenGenerator tokenGenerator;

    public LoginAuthenticationFilter(
            AuthenticationManager authenticationManager,
            MemberService memberService,
            JwtTokenGenerator tokenGenerator
    ) {
        super(authenticationManager);
        this.memberService = memberService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            // ObjectMapper() 사용시 기본 생성자가 필요.
            LoginMemberReqDto creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginMemberReqDto.class);

            log.debug("creds id ={}, password = {}", creds.getLoginId(), creds.getPassword());
            // loginId, password, 권한목록 인증을 시도한다, 성공 : Authentication 객체 반환함.
            return this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLoginId(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch(IOException e) {
            // ObjectMapper에서 JSON 타입 오류, I/O 오류, JSON 처리 오류 발생 시
            log.error("INVALID_INPUT_VALUE = {}", e.toString());
            throw new BusinessException(INVALID_INPUT_VALUE);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException  {
        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();
        Member member = principal.getMember();
        memberService.getMemberVoByLoginId(member.getMemberId());
        String token = tokenGenerator.generateToken(member);

        response.addHeader("Authorization", "Bearer " + token);
    }
}