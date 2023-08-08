package com.gxdxx.instagram.domain.comment.application;

import com.gxdxx.instagram.domain.comment.dao.CommentRepository;
import com.gxdxx.instagram.domain.comment.domain.Comment;
import com.gxdxx.instagram.domain.comment.dto.request.CommentRegisterRequest;
import com.gxdxx.instagram.domain.comment.dto.response.CommentRegisterResponse;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.post.exception.PostNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentCreateServiceTest {

    @InjectMocks
    private CommentCreateService commentCreateService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;

    private static final String NICKNAME = "nickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final Long POST_ID = 1L;
    private static final String POST_CONTENT = "postContent";
    private static final String POST_IMAGE_URL = "postImageUrl";
    private static final String COMMENT_CONTENT = "commentContent";

    @Test
    @DisplayName("[댓글 등록] - 성공")
    void createComment_withValidRequest_shouldSucceed() {
        // given
        CommentRegisterRequest request = createCommentRegisterRequest();
        User user = createUser();
        Post post = createPost(user);
        Comment comment = createComment(user, post);

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentRegisterResponse response = commentCreateService.createComment(request, NICKNAME);

        // then
        assertEquals(request.content(), response.content());
    }

    @Test
    @DisplayName("[댓글 등록] - 실패 (존재하지 않는 유저)")
    void createComment_withNonExistingUser_shouldThrowUserNotFoundException() {
        // given
        CommentRegisterRequest request = createCommentRegisterRequest();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> commentCreateService.createComment(request, NICKNAME));
    }

    @Test
    @DisplayName("[댓글 등록] - 실패 (존재하지 않는 게시글)")
    void createComment_withNonExistingPost_shouldThrowPostNotFoundException() {
        // given
        CommentRegisterRequest request = createCommentRegisterRequest();
        User user = createUser();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(PostNotFoundException.class, () -> commentCreateService.createComment(request, NICKNAME));
    }

    private User createUser() {
        return User.of(NICKNAME, ENCODED_PASSWORD, PROFILE_IMAGE_URL);
    }

    private Post createPost(User user) {
        return Post.of(POST_CONTENT, POST_IMAGE_URL, user);
    }

    private Comment createComment(User user, Post post) {
        return Comment.of(COMMENT_CONTENT, user, post);
    }

    private CommentRegisterRequest createCommentRegisterRequest() {
        return new CommentRegisterRequest(POST_ID, COMMENT_CONTENT);
    }

}