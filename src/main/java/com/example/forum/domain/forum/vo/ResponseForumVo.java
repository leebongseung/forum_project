package com.example.forum.domain.forum.vo;

import com.example.forum.domain.forum.entity.Forum;
import com.example.forum.domain.member.vo.ResponseMember;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResponseForumVo {
    private Long id; // 게시판 id
    private ResponseMember author; // 작성자
    private String title; // 제목
    private String content; // 내용
    private Long views; // 조회수
    private Long likes; // 좋아요 수
    private LocalDateTime createAt; // 생성일
    private LocalDateTime updateAt; // 수정일

    public static ResponseForumVo of(Forum forum){
        ResponseForumVo responseForumVo = new ResponseForumVo();
        responseForumVo.author = new ResponseMember(forum.getAuthor());
        responseForumVo.id = forum.getId();
        responseForumVo.title = forum.getTitle();
        responseForumVo.content = forum.getContent();
        responseForumVo.views = forum.getViews();
        responseForumVo.likes = forum.getLikes();
        responseForumVo.createAt = forum.getCreateAt();
        responseForumVo.updateAt = forum.getUpdateAt();
        return responseForumVo;
    }
}
