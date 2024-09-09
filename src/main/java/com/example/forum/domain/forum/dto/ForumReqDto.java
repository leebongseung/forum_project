package com.example.forum.domain.forum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.example.forum.domain.forum.entity.ForumEntityConstants.CONTENT_MAX_LENGTH;
import static com.example.forum.domain.forum.entity.ForumEntityConstants.TITLE_MAX_LENGTH;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ForumReqDto {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(max = TITLE_MAX_LENGTH, message = "제목은 최대 " + TITLE_MAX_LENGTH + "자 입니다.")
    private String title; // 제목

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = CONTENT_MAX_LENGTH, message = "내용은 최대 " + CONTENT_MAX_LENGTH + "자 입니다.")
    private String content; // 내용
}
