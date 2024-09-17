package com.example.forum.domain.member.entity;

import com.example.forum.common.entity.BaseEntity;
import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Member extends BaseEntity implements Serializable {
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
    private String name; // 닉네임

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role; // 권한

    @OneToMany(mappedBy = "author")
    private List<Forum> forums;

    public static Member of(SignUpMemberReqDto signUpMemberReqDto) {
        Member member = new Member();
        member.name = signUpMemberReqDto.getName();
        member.memberId = UUID.randomUUID().toString();
        member.loginId = signUpMemberReqDto.getLoginId();
        member.password = signUpMemberReqDto.getPassword();
        member.email = signUpMemberReqDto.getEmail();
        member.phoneNumber = signUpMemberReqDto.getPhoneNumber();
        member.role = MemberRole.MEMBER;
        return member;
    }

    public void passwordEncryption(String encryptedPasswords) {
        this.password = encryptedPasswords;
    }
}
