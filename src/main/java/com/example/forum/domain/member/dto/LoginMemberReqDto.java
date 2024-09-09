package com.example.forum.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class LoginMemberReqDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String loginId; // 로그인 아이디

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 로그인 비밀번호
}
