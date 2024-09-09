package com.example.forum.domain.member.service;

import com.example.forum.common.error.ErrorCode;
import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.config.jwt.JwtTokenGenerator;
import com.example.forum.config.security.CustomUserDetails;
import com.example.forum.domain.member.dto.LoginMemberReqDto;
import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.repository.MemberRepository;
import com.example.forum.domain.member.vo.ResponseMember;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.forum.common.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenGenerator tokenGenerator;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 사용자 이름을 기반으로 사용자를 찾습니다. 실제 구현에서 검색은 implementation 인스턴스가 구성되는 방식에 따라 대소문자를 구분하거나 대소문자를 구분하지 않을 수 있습니다. 이 경우 UserDetails 반환되는 개체에는 실제로 요청된 것과 다른 경우의 사용자 이름이 있을 수 있습니다.
     *
     * @param loginId – 데이터가 필요한 사용자를 식별하는 사용자 이름입니다.
     * @return 완전히 채워진 사용자 레코드(사용 안 함 null)
     * @throws BusinessException(ErrorCode) – 사용자를 찾을 수 없거나 GrantedAuthority가 없는 경우 MEMBER_NOT_FOUND 리턴
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) {
        return CustomUserDetails.of(repository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND)));
    }

    /**
     * 회원 가입을 처리하는 메서드.
     * @param signUpMemberReqDto 회원 가입 요청 정보를 담고 있는 DTO
     * @return 가입된 회원 정보를 반환하는 ResponseMember 객체
     * @throws BusinessException 이메일 또는 아이디 중복 시 예외 발생
     */
    @Override
    public ResponseMember signUp(SignUpMemberReqDto signUpMemberReqDto) {
        Member member = Member.of(signUpMemberReqDto);

        // 이메일 중복 여부 확인
        Optional<Member> byEmail = repository.findByEmail(member.getEmail());
        if (byEmail.isPresent()) throw new BusinessException(EMAIL_DUPLICATION);

        // 아이디 중복 여부 확인
        Optional<Member> byLoginId = repository.findByLoginId(member.getLoginId());
        if (byLoginId.isPresent()) throw new BusinessException(LOGIN_ID_DUPLICATION);

        // 암호화하기
        member.passwordEncryption(passwordEncoder.encode(member.getPassword()));

        // 성공적인 회원 가입
        repository.save(member);

        // 결과 반환
        return new ResponseMember(member);
    }

    /**
     * 회원 로그인을 처리하는 메서드.
     * @param response HTTP 응답 객체, 인증 토큰을 헤더에 추가하기 위해 사용
     * @param loginMemberReqDto 로그인 요청 정보를 담고 있는 DTO
     * @return 로그인된 회원 정보를 반환하는 ResponseMember 객체
     */
    @Override
    public ResponseMember login(HttpServletResponse response, LoginMemberReqDto loginMemberReqDto) {
        // 1. loginId + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginMemberReqDto.getLoginId(), loginMemberReqDto.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = customUserDetails.getMember();
        String token = tokenGenerator.generateToken(member);
        response.addHeader("Authorization", "Bearer " + token);
        return new ResponseMember(member);
    }

    /**
     * 주어진 회원 ID로 회원 정보를 조회하는 메서드.
     * @param memberId 조회할 회원의 ID
     * @return 조회된 회원 정보
     * @throws BusinessException 주어진 ID에 해당하는 회원이 존재하지 않을 경우 예외 발생
     */
    @Override
    public Member getMemberByMemberId(String memberId) {
        log.debug("memberId = {}", memberId);
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }
}
