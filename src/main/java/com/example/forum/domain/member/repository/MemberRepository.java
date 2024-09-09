package com.example.forum.domain.member.repository;

import com.example.forum.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일 조회
    Optional<Member> findByEmail(String email);

    // 아이디 조회
    Optional<Member> findByLoginId(String loginId);

    // 아이디 + 비밀번호 조회
    Optional<Member> findByLoginIdAndPassword(String loginId, String password);

    // 멤버 아이디 조회
    Optional<Member> findByMemberId(String memberId);
}
