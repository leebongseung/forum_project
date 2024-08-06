package com.example.forum.domain.member.service;

import com.example.forum.common.error.ErrorCode;
import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.config.security.CustomUserDetails;
import com.example.forum.domain.member.dto.SignUpMemberReqDto;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.repository.MemberRepository;
import com.example.forum.domain.member.vo.ResponseMemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
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

    @Override
    public ResponseMemberVo signUp(SignUpMemberReqDto signUpMemberReqDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE) //setter 없이 자동 매핑 처리
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        Member member = mapper.map(signUpMemberReqDto, Member.class);

        // 이메일 중복 여부 확인
        Optional<Member> byEmail = repository.findByEmail(member.getEmail());
        if (byEmail.isPresent()) throw new BusinessException(EMAIL_DUPLICATION);

        // 아이디 중복 여부 확인
        Optional<Member> byLoginId = repository.findByLoginId(member.getLoginId());
        if (byLoginId.isPresent()) throw new BusinessException(LOGIN_ID_DUPLICATION);

        // 암호화하기
        member.passwordEncryption(passwordEncoder.encode(member.getPassword()));

        // MemberId 생성하기
        member.settingUpAMember();

        // 성공적인 회원 가입
        repository.save(member);

        // 결과 반환
        return mapper.map(member, ResponseMemberVo.class);
    }

    @Override
    public ResponseMemberVo getMemberByLoginId(String loginId) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE) //setter 없이 자동 매핑 처리
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        Member member = repository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return mapper.map(member, ResponseMemberVo.class);
    }


    /**
     * 사용자 이름을 기반으로 사용자를 찾습니다. 실제 구현에서 검색은 implementation 인스턴스가 구성되는 방식에 따라 대소문자를 구분하거나 대소문자를 구분하지 않을 수 있습니다. 이 경우 UserDetails 반환되는 개체에는 실제로 요청된 것과 다른 경우의 사용자 이름이 있을 수 있습니다.
     *
     * @param username – 데이터가 필요한 사용자를 식별하는 사용자 이름입니다.
     * @return 완전히 채워진 사용자 레코드(사용 안 함 null)
     * @throws BusinessException(ErrorCode)
     *  – 사용자를 찾을 수 없거나 GrantedAuthority가 없는 경우 MEMBER_NOT_FOUND 리턴
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        return CustomUserDetails.of(repository.findByLoginId(username)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND)));
    }
}
