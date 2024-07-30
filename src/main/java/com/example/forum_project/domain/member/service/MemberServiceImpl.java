package com.example.forum_project.domain.member.service;

import com.example.forum_project.common.error.exception.BusinessException;
import com.example.forum_project.domain.member.dto.SignUpMemberReqDto;
import com.example.forum_project.domain.member.entity.Member;
import com.example.forum_project.domain.member.repository.MemberRepository;
import com.example.forum_project.domain.member.vo.ResponseMemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.forum_project.common.error.ErrorCode.EMAIL_DUPLICATION;
import static com.example.forum_project.common.error.ErrorCode.LOGIN_ID_DUPLICATION;

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
}
