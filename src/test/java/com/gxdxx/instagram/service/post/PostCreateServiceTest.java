package com.gxdxx.instagram.service.post;

import com.gxdxx.instagram.domain.post.application.PostCreateService;
import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.domain.post.domain.Post;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.post.dao.PostRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.config.s3.S3Uploader;
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

    @Test
    @DisplayName("[게시물 등록] - 성공")
    void registerPost_withValidRequest_shouldSucceed() {
        PostRegisterRequest request = createPostRegisterRequest();
        User user = createUser();
        String imageUrl = "imageUrl";
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        when(s3Uploader.upload(any(), anyString())).thenReturn(imageUrl);
        when(postRepository.save(any(Post.class))).thenReturn(Post.of(request.content(), imageUrl, user));

        PostRegisterResponse response = postCreateService.createPost(request, user.getNickname());

        assertEquals(request.content(), response.content());
        assertEquals(imageUrl, response.imageUrl());
    }

    @Test
    @DisplayName("[게시물 등록] - 실패 (존재하지 않는 유저)")
    void registerPost_withNonExistingUser_shouldThrowUserNotFoundException() {
        PostRegisterRequest request = createPostRegisterRequest();
        User user = createUser();
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> postCreateService.createPost(request, user.getNickname()));
    }

    private User createUser() {
        return User.of("nickname", "encodedPassword", "profileImageUrl");
    }

    private PostRegisterRequest createPostRegisterRequest() {
        MockMultipartFile mockFile = getMockMultipartFile();
        String content = "content";
        return new PostRegisterRequest(content, mockFile);
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
