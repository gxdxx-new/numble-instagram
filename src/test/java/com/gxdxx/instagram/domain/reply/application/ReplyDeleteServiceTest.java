package com.gxdxx.instagram.domain.reply.application;

import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.reply.dao.ReplyRepository;
import com.gxdxx.instagram.domain.reply.domain.Reply;
import com.gxdxx.instagram.domain.reply.exception.ReplyNotFoundException;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplyDeleteServiceTest {

    @InjectMocks
    private ReplyDeleteService replyDeleteService;
    @Mock
    private ReplyRepository replyRepository;

    private static final String CORRECT_NICKNAME = "correctNickname";
    private static final String INCORRECT_NICKNAME = "incorrectNickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String POST_CONTENT = "postContent";
    private static final String POST_IMAGE_URL = "postImageUrl";
    private static final String COMMENT_CONTENT = "commentContent";
    private static final Long REPLY_ID = 1L;
    private static final String REPLY_CONTENT = "replyContent";
    private static final String SUCCESS_MESSAGE = "200 SUCCESS";

    @Test
    @DisplayName("[답글 삭제] - 성공")
    void deleteReply_withValidRequest_shouldSucceed() {
        // given
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);
        Reply reply = createReply(user, comment);

        when(replyRepository.findById(REPLY_ID)).thenReturn(Optional.of(reply));

        // when
        SuccessResponse response = replyDeleteService.deleteReply(REPLY_ID, CORRECT_NICKNAME);

        // then
        assertEquals(SUCCESS_MESSAGE, response.successMessage());

        verify(replyRepository).delete(reply);
    }

    @Test
    @DisplayName("[답글 삭제] - 실패 (존재하지 않는 답글)")
    void deleteReply_withNonExistingReply_shouldThrowReplyNotFoundException() {
        // given
        when(replyRepository.findById(REPLY_ID)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(ReplyNotFoundException.class, () -> replyDeleteService.deleteReply(REPLY_ID, CORRECT_NICKNAME));
    }

    @Test
    @DisplayName("[답글 삭제] - 실패 (요청자와 답글 작성자 불일치)")
    void deleteReply_withNotMatchReplyWriter_shouldThrowUnauthorizedAccessException() {
        // given
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);
        Reply reply = createReply(user, comment);

        when(replyRepository.findById(REPLY_ID)).thenReturn(Optional.of(reply));

        // when & then
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> replyDeleteService.deleteReply(REPLY_ID, INCORRECT_NICKNAME));
    }

    private User createUser(String nickname) {
        return User.of(nickname, ENCODED_PASSWORD, PROFILE_IMAGE_URL);
    }

    private Post createPost(User user) {
        return Post.of(POST_CONTENT, POST_IMAGE_URL, user);
    }

    private Comment createComment(User user, Post post) {
        return Comment.of(COMMENT_CONTENT, user, post);
    }

    private Reply createReply(User user, Comment comment) {
        return Reply.of(REPLY_CONTENT, user, comment);
    }

}