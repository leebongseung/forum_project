package com.example.forum.config;

import com.example.forum.config.exceptionhandler.CustomAuthenticationEntryPoint;
import com.example.forum.config.filter.LoginAuthenticationFilter;
import com.example.forum.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final MemberService memberService;
    private final PassWordEncoderConfig passwordEncoder;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(memberService)
                .passwordEncoder(passwordEncoder.passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();


        // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
        http.csrf(AbstractHttpConfigurer::disable);

        // 요청 권한 경로 설정하기
        http.authorizeHttpRequests(a -> a.requestMatchers("/**").permitAll())
                .authenticationManager(authenticationManager)
                .addFilter(getAuthenticationFilter(authenticationManager))
                // errorHandler
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint))


                // 프레임 옵션 보안 헤더가 비활성화, 웹 페이지를 렌더링 해야하는 경우 필요하다!
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

    private LoginAuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        return new LoginAuthenticationFilter(authenticationManager, memberService, env);
    }


}
