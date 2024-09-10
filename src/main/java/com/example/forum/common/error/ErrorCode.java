package com.example.forum.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // enum -> json 형태로 직렬화 가능.
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", " 유효하지 않은 입력 값"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "C002", "허용되지 않은 메소드"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "C003", " 엔터티를 찾을 수 없음"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "C005", " 잘못된 형식 값"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "C006", "액세스가 거부되었습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "C007", "리소스를 찾을 수 없습니다."),

    // Member
    EMAIL_DUPLICATION(HttpStatus.CONFLICT.value(), "M001", "이메일이 중복됩니다."),
    LOGIN_ID_DUPLICATION(HttpStatus.CONFLICT.value(), "M002", "로그인 아이디가 중복됩니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST.value(), "M003", "로그인 입력이 잘못되었습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "M004", "사용자를 찾을 수 없거나 권한이 없습니다."),

    // authentication, approval
    INVALID_TOKEN(HttpStatus.BAD_REQUEST.value(), "A001", "잘못된 형식의 토큰"),
    SC_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "A002", "유효한 자격이 없음."),

    // forum
    FORUM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "F001", "게시글을 찾을 수 없습니다."),
    FORUM_COUNT_SCHEDULING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "F002", "조회수 스케줄러 오류")
    ;


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }


}