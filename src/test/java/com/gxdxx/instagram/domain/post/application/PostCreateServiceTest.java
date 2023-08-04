package com.gxdxx.instagram.domain.post.application;

import com.gxdxx.instagram.domain.post.application.PostCreateService;
import com.gxdxx.instagram.domain.post.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.domain.post.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.global.config.s3.S3Uploader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostCreateServiceTest {

    @InjectMocks
    private PostCreateService postCreateService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Uploader s3Uploader;

    private static final String NICKNAME = "nickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String PROFILE_IMAGE_URL = "profileImageUrl";
    private static final String IMAGE_URL = "imageUrl";
    private static final String CONTENT = "content";
    private static final String DIR_NAME = "images";

    @Test
    @DisplayName("[게시물 등록] - 성공")
    void registerPost_withValidRequest_shouldSucceed() {
        // given
        PostRegisterRequest request = createPostRegisterRequest();
        User user = createUser();

        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        when(s3Uploader.upload(request.image(), DIR_NAME)).thenReturn(IMAGE_URL);
        when(postRepository.save(any(Post.class))).thenReturn(Post.of(request.content(), IMAGE_URL, user));

        // when
        PostRegisterResponse response = postCreateService.createPost(request, user.getNickname());

        // then
        assertEquals(request.content(), response.content());
        assertEquals(IMAGE_URL, response.imageUrl());
    }

    @Test
    @DisplayName("[게시물 등록] - 실패 (존재하지 않는 유저)")
    void registerPost_withNonExistingUser_shouldThrowUserNotFoundException() {
        // given
        PostRegisterRequest request = createPostRegisterRequest();
        User user = createUser();

        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> postCreateService.createPost(request, user.getNickname()));
    }

    private User createUser() {
        return User.of(NICKNAME, ENCODED_PASSWORD, PROFILE_IMAGE_URL);
    }

    private PostRegisterRequest createPostRegisterRequest() {
        MockMultipartFile mockFile = getMockMultipartFile();
        return new PostRegisterRequest(CONTENT, mockFile);
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
