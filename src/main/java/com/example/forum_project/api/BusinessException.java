package com.example.forum_project.api;


import lombok.Getter;

/*
   기능별 커스텀 에러 응답을 준다.
*/
@Getter
public class BusinessException extends RuntimeException { // 상속해서 depth를 늘리는 설계

    private ErrorCode errorCode;

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}