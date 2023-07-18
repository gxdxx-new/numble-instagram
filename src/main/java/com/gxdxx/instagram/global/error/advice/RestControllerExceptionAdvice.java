package com.gxdxx.instagram.global.error.advice;

import com.gxdxx.instagram.domain.chatroom.exception.ChatRoomNotFoundException;
import com.gxdxx.instagram.domain.comment.exception.CommentNotFoundException;
import com.gxdxx.instagram.domain.follow.exception.FollowAlreadyExistsException;
import com.gxdxx.instagram.domain.follow.exception.FollowNotFountException;
import com.gxdxx.instagram.domain.post.exception.PostNotFoundException;
import com.gxdxx.instagram.domain.reply.exception.ReplyNotFoundException;
import com.gxdxx.instagram.domain.user.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.domain.user.exception.PasswordNotMatchException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.global.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.auth.RefreshTokenInvalidException;
import com.gxdxx.instagram.global.auth.UnauthorizedAccessException;
import com.gxdxx.instagram.global.error.FileProcessingException;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import jakarta.validation.ConstraintViolationException;
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

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ErrorResponse chatRoomNotFoundException(ChatRoomNotFoundException ex) {
        return new ErrorResponse("존재하지 않는 채팅방입니다.");
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ErrorResponse invalidRequestException(InvalidRequestException ex) {
        return new ErrorResponse("입력값을 확인해주세요.");
    }

    @ExceptionHandler(FileProcessingException.class)
    public ErrorResponse fileProcessingException(FileProcessingException ex) {
        return new ErrorResponse("파일 업로드 중 문제가 발생했습니다.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse constraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponse("문제가 발생했습니다. 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ErrorResponse refreshTokenInvalidException(RefreshTokenInvalidException ex) {
        return new ErrorResponse("유효하지 않은 Refresh Token입니다.");
    }

}
