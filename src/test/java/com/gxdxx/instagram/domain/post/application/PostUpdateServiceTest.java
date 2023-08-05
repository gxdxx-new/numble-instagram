package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.post.dto.request.PostUpdateRequest;
import com.gxdxx.instagram.domain.post.dto.response.PostUpdateResponse;
import com.gxdxx.instagram.domain.post.exception.PostNotFoundException;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.global.auth.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.global.config.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostUpdateServiceTest {

    @InjectMocks
    private PostUpdateService postUpdateService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private S3Uploader s3Uploader;

    private static final Long POST_ID = 1L;
    private static final String NICKNAME = "nickname";
    private static final String ANOTHER_NICKNAME = "anotherNickname";
    private static final String PASSWORD = "password";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String CONTENT = "content";
    private static final String NEW_CONTENT = "newContent";
    private static final String IMAGE_URL = "imageUrl";
    private static final String NEW_IMAGE_URL = "newImageUrl";
    private static final String DIR_NAME = "images";


    @Test
    @DisplayName("[게시글 수정] - 성공")
    void updatePost_withValidRequest_shouldSucceed() {
        // given
        PostUpdateRequest request = createPostUpdateRequest();
        Post updatePost = createPost();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(updatePost));
        when(s3Uploader.upload(request.image(), DIR_NAME)).thenReturn(NEW_IMAGE_URL);

        // when
        PostUpdateResponse response = postUpdateService.updatePost(request, NICKNAME);

        // then
        assertEquals(NEW_CONTENT, response.content());
        assertEquals(NEW_IMAGE_URL, response.imageUrl());
    }

    @Test
    @DisplayName("[게시글 수정] - 실패 (존재하지 않는 게시글)")
    void updatePost_PostNotFoundException() {
        // given
        PostUpdateRequest request = createPostUpdateRequest();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> postUpdateService.updatePost(request, NICKNAME));
    }

    @Test
    @DisplayName("[게시글 수정] - 실패 (게시글 작성자와 요청자가 다른 경우)")
    void updatePost_UnauthorizedAccessException() {
        // given
        PostUpdateRequest request = createPostUpdateRequest();
        Post updatePost = createPost();

        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(updatePost));

        // when & then
        assertThrows(UnauthorizedAccessException.class, () -> postUpdateService.updatePost(request, ANOTHER_NICKNAME));
    }

    private User createUser() {
        return User.of(NICKNAME, PASSWORD, PROFILE_IMAGE_URL);
    }

    private Post createPost() {
        return Post.of(CONTENT, IMAGE_URL, createUser());
    }

    private PostUpdateRequest createPostUpdateRequest() {
        return new PostUpdateRequest(POST_ID, NEW_CONTENT, getMockMultipartFile());
    }

    private MockMultipartFile getMockMultipartFile() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test content".getBytes()
        );
        return mockFile;
    }

}