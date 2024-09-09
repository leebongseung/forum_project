package com.example.forum.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenParser {
    private final Environment env;

    /**
     * 토큰 검증하는 메서드
     *
     * @return true 검증 성공 / false 검증 실패
     */
    public boolean validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode((env.getProperty("token.secret"))));

        try {
            // Jwt 토큰 복호화
            Jwts
                    .parser()
                    .verifyWith(key) // 서명 검증
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * request 헤더에서 AccessToken 추출하는 메서드
     *
     * @param request
     * @return String : AccessToken
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        return resolveToken(bearerToken);
    }

    /**
     * Token의 Bearer유형의 토큰 접두사 를 제거하는 메서드
     *
     * @param bearerToken
     * @return String : RefreshToken
     */
    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
