package com.example.forum.domain.forum.service;

import com.example.forum.common.error.ErrorCode;
import com.example.forum.common.error.exception.BusinessException;
import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.forum.repository.ForumRepository;
import com.example.forum.domain.forum.vo.ResponseForum;
import com.example.forum.domain.member.entity.Member;
import com.example.forum.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ForumServiceImpl implements ForumService {

    private final MemberService memberService;
    private final ForumRepository repository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseForum createForum(
            ForumReqDto forumReqDto,
            String memberId
    ) {
        Member member = memberService.getMemberByMemberId(memberId);
        Forum forum = Forum.of(forumReqDto, member);
        repository.save(forum);
        return new ResponseForum(forum);
    }

    @Override
    @Transactional
    public ResponseForum updateForum(
            String forumId,
            ForumReqDto forumReqDto,
            String memberId
    ) {
        // 1. forum 조회하기
        Forum forum = getForumByForumId(forumId);

        // 2. 작성자 인지 확인하기
        validateForumAuthor(forum, memberId);

        // 3. 변경 지점만 변경하기 -> update 수행.
        forum.updateForum(forumReqDto);

        return new ResponseForum(forum);
    }

    @Override
    @Transactional
    public void deleteForum(
            String forumId,
            String memberId
    ) {
        Forum forum = getForumByForumId(forumId);
        validateForumAuthor(forum, memberId);
        repository.delete(forum);
    }

    @Override
    public void incrementViewCount(String forumId) {
        redisTemplate.opsForValue().increment("forum:viewCount:" + forumId);
    }

    @Override
    public void likeForum(String forumId, String memberId) {

    }

    /**
     * 주어진 forumId에 해당하는 포럼을 조회하는 메서드.
     *
     * @param forumId 조회할 포럼의 ID
     * @return forumId에 해당하는 Forum 객체
     * @throws BusinessException 해당 forumId로 포럼을 찾지 못할 경우 FORUM_NOT_FOUND 오류를 발생시킴
     */
    public Forum getForumByForumId(String forumId) {
        return repository.findByForumId(forumId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FORUM_NOT_FOUND));
    }

    /**
     * 주어진 포럼의 작성자와 사용자의 ID가 일치하는지 검증하는 메서드.
     *
     * @param forum 검증할 포럼 객체
     * @param memberId 검증할 사용자의 ID
     * @throws BusinessException 작성자와 사용자가 일치하지 않으면 FORUM_NOT_FOUND 예외를 발생시킴
     */
    public void validateForumAuthor(Forum forum, String memberId) {
        if (forum.getAuthor().getMemberId().equals(memberId)) {
            return;
        }
        throw new BusinessException(ErrorCode.FORUM_NOT_FOUND);
    }
}
