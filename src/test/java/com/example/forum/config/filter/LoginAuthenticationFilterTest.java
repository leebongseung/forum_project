package com.example.forum.config.filter;

import com.example.forum.config.security.CustomUserDetails;
import com.example.forum.domain.member.dto.LoginMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.service.MemberService;
import com.example.forum.domain.member.vo.ResponseMemberVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationFilterTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MemberService memberService;
    @Mock
    private Environment env;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginAuthenticationFilter loginAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE) //setter 없이 자동 매핑 처리
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

    }

    @Test
    @DisplayName("[성공] 로그인 시 정상적인 값이 들어오는 경우")
    void attemptAuthentication() throws JsonProcessingException {
        // given
        LoginMemberReqDto creds = LoginMemberReqDto.of("testuser", "password");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(creds);
        request.setContent(json.getBytes());

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password", new ArrayList<>()));

        // when
        Authentication result = loginAuthenticationFilter.attemptAuthentication(request, response);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("[성공] 로그인 시 정상적인 값이 들어오는 경우")
    void successfulAuthentication() throws IOException, ServletException {
        // given
        LoginMemberReqDto creds = LoginMemberReqDto.of("testuser", "password");
        Member member = mapper.map(creds, Member.class);
        String secretKeyBase64 = "c2VjcmV0"; // "secret"의 BASE64 인코딩 값
        // SecretKey secretKey = new SecretKeySpec("secret".getBytes(), "HmacSHA256");

        when(authentication.getPrincipal()).thenReturn(CustomUserDetails.of(member));
        when(memberService.getMemberByLoginId("testuser")).thenReturn(ResponseMemberVo.of("1","testuser","test@test.com","test"));
        when(env.getProperty("token.secret")).thenReturn(secretKeyBase64);
        when(Decoders.BASE64.decode(secretKeyBase64)).thenReturn("secret".getBytes());

        //when
        loginAuthenticationFilter.successfulAuthentication(request, response, filterChain, authentication);

        //then
        assertThat(response.getHeader("Authorization")).isNotBlank();
        // Bearer 로 시작하는 token인 지 검증
        assertThat(response.getHeader("Authorization")).startsWith("Bearer ");
    }
}