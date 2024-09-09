package com.example.forum.config.jwt;

import com.example.forum.config.security.CustomUserDetails;
import com.example.forum.domain.member.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {
    private final Environment env;
    private static final String AUTHORITIES_KEY = "auth";

    public String generateToken(Member member)
    {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode((env.getProperty("token.secret"))));
        CustomUserDetails customUserDetails = CustomUserDetails.of(member);
        String authorities = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                // jwt 토큰에 포함할 subject
                .subject(member.getMemberId())
                // 권한 정보 저장
                .claim(AUTHORITIES_KEY, authorities)
                .expiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration_time")))))
                // 토큰 서명하기
                .signWith(key)
                .compact();
    }
}
