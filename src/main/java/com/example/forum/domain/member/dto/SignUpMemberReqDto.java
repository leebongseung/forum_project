package com.example.forum.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class SignUpMemberReqDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-z0-9_-]+$", message = "아이디는 영문 소문자, 숫자, 하이픈(-), 언더스코어(_)만 사용 가능합니다.")
    private String loginId; // 로그인 아이디

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 최소 1개 이상 포함하며, 10자 이상이어야 합니다.")
    private String password; // 로그인 비밀번호


    @NotBlank(message = "이메일은 필수 입력값 입니다")
    @Email(message = "올바른 이메일 양식이 아닙니다")
    private String email; // 이메일

    //숫자와 하이폰으로 구성된 형식 검즘
    @Pattern(regexp = "^(01[0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$", message = "유효한 휴대폰 번호 형식이 아닙니다.")
    @NotBlank(message = "휴대폰번호는 필수 입력값 입니다.")
    private String phoneNumber; // 휴대폰 번호

    @NotBlank(message = "이름은 필수 입력값 입니다.")
    @Size(max = 4, message = "최소 4자 이하의 이름만 입력가능합니다.")
    private String name; // 작성자
}
