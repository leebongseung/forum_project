package com.example.forum.config.filter;

import com.example.forum.common.error.ErrorCode;
import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.config.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORITIES_KEY = "auth";
    private final Environment env;
    private final JwtTokenParser jwtTokenParser;

    /**
     * Jwt 토큰의 인증정보를 SecurityContext에 저장하는 역할을 수행
     * 실제 필터링 로직은 여기에 작성
     */

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtTokenParser.resolveAccessToken(httpServletRequest);

        String requestURI = httpServletRequest.getRequestURI();
        log.trace("현재 uri 주소는 = {}", httpServletRequest.getRequestURI());

        // 1. Request header accessToken 토큰 추출 및 유효성 검사
        if (StringUtils.hasText(accessToken) && jwtTokenParser.validateToken(accessToken)) {
            authenticateFromToken(accessToken, requestURI);
        }

        // 2. Request header accessToken 토큰 추출 결과가 false 일 경우
        // 엑세스 토큰이 만료되었다고 401 에러를 던져야 함.
        else if (StringUtils.hasText(accessToken) && !jwtTokenParser.validateToken(accessToken)) {
            log.debug("토큰이 만료 되었습니다!");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러 설정
            httpServletResponse.getWriter().write("Unauthorized: Access is denied due to invalid credentials");
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 토큰에서 Authentication 객체를 추출하여 SecurityContextHolder에 저장하는 메서드
     *
     * @param accessToken
     * @param requestURI
     */
    private void authenticateFromToken(String accessToken, String requestURI) {
        Authentication authentication = getAuthentication(accessToken);

        // 요청이 들어오는 순간 SecurityContextHolder에 authentication 가 저장됨
        // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
    }

    /**
     * accessToken을 복호화 해서 userId를 얻어오는 메서드
     *
     * @param accessToken
     * @return Authentication
     */
    public Authentication getAuthentication(String accessToken) {

        // Jwt 토큰 복호화
        Claims claims = getClaimsFromAccessToken(accessToken);
        String memberId = String.valueOf(claims.getSubject());

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        return new UsernamePasswordAuthenticationToken(memberId, accessToken, authorities);
    }

    /**
     * accessToken에서 서명 검증 및 Claims 반환하는 메서드
     *
     * @param accessToken 엑세스 토큰
     * @return Claims 사용자에 대한 정보
     */
    private Claims getClaimsFromAccessToken(String accessToken) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode((env.getProperty("token.secret"))));

        Jws<Claims> claimsJws = Jwts
                .parser()
                .verifyWith(key) // 서명 검증
                .build()
                .parseSignedClaims(accessToken);

        Claims claims = claimsJws.getPayload();

        if (claims.get(AUTHORITIES_KEY) == null) {
            log.info("권한 정보가 없는 토큰입니다");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        return claims;
    }

}
