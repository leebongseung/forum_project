package com.example.forum.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcludedJwtPaths {
    public static final Set<String> EXCLUDE_URLS = Set.of(
            "/security-login",
            "/security-login/login",
            "/v3/api-docs/**",
            "/members",
            "/swagger-ui/**",
            "/favicon.ico",
            "/h2-console",
            "/h2-console/**",
            "/login"
    );
}
