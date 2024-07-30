package com.example.forum_project.error;

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


    // Member
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "M001", "이메일이 중복됩니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST.value(), "M002", "로그인 입력이 잘못되었습니다."),


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