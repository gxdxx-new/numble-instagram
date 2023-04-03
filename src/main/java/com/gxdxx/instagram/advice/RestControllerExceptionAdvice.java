package com.gxdxx.instagram.advice;

import com.gxdxx.instagram.dto.response.ErrorResponse;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ErrorResponse nicknameAlreadyExistsException(NicknameAlreadyExistsException ex) {
        return new ErrorResponse("중복된 닉네임입니다.");
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ErrorResponse invalidRequestException(InvalidRequestException ex) {
        return new ErrorResponse("입력값을 확인해주세요.");
    }

}
