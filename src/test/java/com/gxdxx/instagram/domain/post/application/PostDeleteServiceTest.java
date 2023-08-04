package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.post.exception.PostNotFoundException;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostDeleteServiceTest {

    @InjectMocks
    private PostDeleteService postDeleteService;
    @Mock
    private PostRepository postRepository;

    private static final Long POST_ID = 1L;
    private static final String NICKNAME = "nickname";
    private static final String ANOTHER_NICKNAME = "anotherNickname";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String CONTENT = "content";
    private static final String IMAGE_URL = "imageUrl";
    private static final String SUCCESS_MESSAGE = "200 SUCCESS";


    @Test
    @DisplayName("[게시물 삭제] - 성공")
    void deletePost_withValidRequest_shouldSucceed() {
        // given
        Post deletePost = createPost();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(deletePost));

        // when
        SuccessResponse response = postDeleteService.deletePost(POST_ID, NICKNAME);

        // then
        assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[게시물 삭제] - 실패 (존재하지 않는 게시글)")
    void deletePost_PostNotFoundException() {
        // given
        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> postDeleteService.deletePost(POST_ID, NICKNAME));
    }

    @Test
    @DisplayName("[게시물 삭제] - 실패 (게시글 작성자와 요청자가 다른 경우)")
    void deletePost_UnauthorizedAccessException() {
        // given
        Post deletePost = createPost();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(deletePost));

        // when & then
        assertThrows(UnauthorizedAccessException.class, () -> postDeleteService.deletePost(POST_ID, ANOTHER_NICKNAME));
    }

    private User createUser() {
        return User.of(NICKNAME, PASSWORD, PROFILE_IMAGE_URL);
    }

    private Post createPost() {
        return Post.of(CONTENT, IMAGE_URL, createUser());
    }

}