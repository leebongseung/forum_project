package com.example.forum.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");

    private final String key;
}
