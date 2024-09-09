package com.example.forum.domain.forum.service;

import com.example.forum.domain.forum.dto.ForumReqDto;
import com.example.forum.domain.forum.vo.ResponseForumVo;

public interface ForumService {
    ResponseForumVo createForum(ForumReqDto forumReqDto, String memberId);
    ResponseForumVo updateForum(ForumReqDto forumReqDto, String memberId);
    ResponseForumVo deleteForum(Long memberId);
}
