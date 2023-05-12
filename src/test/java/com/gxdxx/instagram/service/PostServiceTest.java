package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.PostRegisterRequest;
import com.gxdxx.instagram.dto.response.PostRegisterResponse;
import com.gxdxx.instagram.entity.Post;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.repository.PostRepository;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("[게시물 등록] - 성공")
    void registerPost_withValidRequest_shouldSucceed() throws IOException {
        PostRegisterRequest request = createPostRegisterRequest();
        User user = createUser();
        String imageUrl = "imageUrl";
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        when(s3Uploader.upload(any(), anyString())).thenReturn(imageUrl);
        when(postRepository.save(any(Post.class))).thenReturn(Post.of(request.content(), imageUrl, user));

        PostRegisterResponse response = postService.registerPost(request, user.getNickname());

        assertEquals(request.content(), response.content());
        assertEquals(imageUrl, response.imageUrl());
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
