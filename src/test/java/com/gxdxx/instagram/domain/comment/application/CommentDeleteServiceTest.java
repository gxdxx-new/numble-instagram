package com.gxdxx.instagram.domain.comment.application;

import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.comment.exception.CommentNotFoundException;
import com.gxdxx.instagram.domain.post.domain.Post;
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
class CommentDeleteServiceTest {

    @InjectMocks
    private CommentDeleteService commentDeleteService;
    @Mock
    private CommentRepository commentRepository;

    private static final String CORRECT_NICKNAME = "correctNickname";
    private static final String INCORRECT_NICKNAME = "incorrectNickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String POST_CONTENT = "postContent";
    private static final String POST_IMAGE_URL = "postImageUrl";
    private static final Long COMMENT_ID = 1L;
    private static final String COMMENT_CONTENT = "commentContent";
    private static final String SUCCESS_MESSAGE = "200 SUCCESS";

    @Test
    @DisplayName("[댓글 삭제] - 성공")
    void deleteComment_withValidRequest_shouldSucceed() {
        // given
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        // when
        SuccessResponse response = commentDeleteService.deleteComment(COMMENT_ID, CORRECT_NICKNAME);

        // then
        assertEquals(SUCCESS_MESSAGE, response.successMessage());

        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("[댓글 삭제] - 실패 (존재하지 않는 댓글)")
    void deleteComment_withNonExistingComment_shouldThrowCommentNotFoundException() {
        // given
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CommentNotFoundException.class, () -> commentDeleteService.deleteComment(COMMENT_ID, CORRECT_NICKNAME));
    }

    @Test
    @DisplayName("[댓글 삭제] - 실패 (요청자와 댓글 작성자 불일치)")
    void deleteComment_withNotMatchCommentWriter_shouldThrowUnauthorizedAccessException() {
        // given
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);

        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        // when & then
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> commentDeleteService.deleteComment(COMMENT_ID, INCORRECT_NICKNAME));
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

}