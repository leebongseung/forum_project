package com.example.forum_project.error.exception;


import com.example.forum_project.error.ErrorCode;
import lombok.Getter;

/*
   기능별 커스텀 에러 응답을 준다.
*/
@Getter
public class BusinessException extends RuntimeException { // 상속해서 depth를 늘리는 설계

    private ErrorCode errorCode; //상속가능하기 때문에 final 아님.

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}