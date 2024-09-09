package com.example.forum.config.exceptionhandler;

import com.example.forum.common.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.forum.common.error.ErrorCode.SC_UNAUTHORIZED;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.debug("CustomAuthenticationEntryPoint 동작 = {}", request.getRequestURI());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // json 직렬화 수행
        String jsonErrorResponse = objectMapper.writeValueAsString(ErrorResponse.of(SC_UNAUTHORIZED));
        response.getWriter().write(jsonErrorResponse);
        response.setStatus(SC_UNAUTHORIZED.getStatus());
    }
}
