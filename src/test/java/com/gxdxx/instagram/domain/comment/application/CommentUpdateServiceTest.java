package com.gxdxx.instagram.domain.comment.application;

import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.comment.dto.request.CommentUpdateRequest;
import com.gxdxx.instagram.domain.comment.dto.response.CommentUpdateResponse;
import com.gxdxx.instagram.domain.comment.exception.CommentNotFoundException;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentUpdateServiceTest {

    @InjectMocks
    private CommentUpdateService commentUpdateService;
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

    @Test
    @DisplayName("[댓글 수정] - 성공")
    void createComment_withValidRequest_shouldSucceed() {
        // given
        CommentUpdateRequest request = createCommentUpdateRequest();
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);

        when(commentRepository.findById(request.id())).thenReturn(Optional.of(comment));

        // when
        CommentUpdateResponse response = commentUpdateService.updateComment(request, CORRECT_NICKNAME);

        // then
        assertEquals(request.content(), response.content());
    }

    @Test
    @DisplayName("[댓글 수정] - 실패 (존재하지 않는 댓글)")
    void createComment_withNonExistingComment_shouldThrowCommentNotFoundException() {
        // given
        CommentUpdateRequest request = createCommentUpdateRequest();

        when(commentRepository.findById(request.id())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(CommentNotFoundException.class, () -> commentUpdateService.updateComment(request, CORRECT_NICKNAME));
    }

    @Test
    @DisplayName("[댓글 수정] - 실패 (요청자와 댓글 작성자 불일치)")
    void createComment_withNotMatchCommentWriter_shouldThrowUnauthorizedAccessException() {
        // given
        CommentUpdateRequest request = createCommentUpdateRequest();
        User user = createUser(CORRECT_NICKNAME);
        Post post = createPost(user);
        Comment comment = createComment(user, post);

        when(commentRepository.findById(request.id())).thenReturn(Optional.of(comment));

        // when & then
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> commentUpdateService.updateComment(request, INCORRECT_NICKNAME));
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

    private CommentUpdateRequest createCommentUpdateRequest() {
        return new CommentUpdateRequest(COMMENT_ID, COMMENT_CONTENT);
    }

}