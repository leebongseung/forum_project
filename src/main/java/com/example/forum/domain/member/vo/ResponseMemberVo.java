package com.example.forum.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@ToString
public class ResponseMemberVo {
    private String memberId;
    private String loginId;
    private String email;
    private String name;
}
