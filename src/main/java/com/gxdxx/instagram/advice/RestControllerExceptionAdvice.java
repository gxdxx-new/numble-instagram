package com.gxdxx.instagram.advice;

import com.gxdxx.instagram.dto.response.ErrorResponse;
import com.gxdxx.instagram.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ErrorResponse nicknameAlreadyExistsException(NicknameAlreadyExistsException ex) {
        return new ErrorResponse("중복된 닉네임입니다.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse userNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse("존재하지 않는 회원입니다.");
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ErrorResponse passwordNotMatchException(PasswordNotMatchException ex) {
        return new ErrorResponse("비밀번호를 확인해주세요.");
    }

    @ExceptionHandler(FollowAlreadyExistsException.class)
    public ErrorResponse followAlreadyExistsException(FollowAlreadyExistsException ex) {
        return new ErrorResponse("이미 팔로우되어 있습니다.");
    }

    @ExceptionHandler(FollowNotFountException.class)
    public ErrorResponse followNotFountException(FollowNotFountException ex) {
        return new ErrorResponse("존재하지 않는 팔로우입니다.");
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorResponse authorizationException(UnauthorizedAccessException ex) {
        return new ErrorResponse("권한이 없습니다.");
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ErrorResponse postNotFoundException(PostNotFoundException ex) {
        return new ErrorResponse("존재하지 않는 게시물입니다.");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ErrorResponse commentNotFoundException(CommentNotFoundException ex) {
        return new ErrorResponse("존재하지 않는 댓글입니다.");
    }

    @ExceptionHandler(ReplyNotFoundException.class)
    public ErrorResponse replyNotFoundException(ReplyNotFoundException ex) {
        return new ErrorResponse("존재하지 않는 답글입니다.");
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ErrorResponse invalidRequestException(InvalidRequestException ex) {
        return new ErrorResponse("입력값을 확인해주세요.");
    }

}
