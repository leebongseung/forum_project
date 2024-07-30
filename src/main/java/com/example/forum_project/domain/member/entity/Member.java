package com.example.forum_project.domain.member.entity;

import com.example.forum_project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId; //멤버 아이디

    @Column(nullable = false, unique = true)
    private String loginId; // 로그인 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false, length = 20) //  E.164 국제 표준 기준 20자 설정
    private String phoneNumber; // 휴대폰 번호

    @Column(nullable = false, length = 30)
    private String name; // 작성자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role; // 권한

    public void settingUpAMember() {
        this.memberId = UUID.randomUUID().toString();
        this.role = MemberRole.MEMBER;
    }

    public void passwordEncryption(String encryptedPasswords) {
        this.password = encryptedPasswords;
    }
}
