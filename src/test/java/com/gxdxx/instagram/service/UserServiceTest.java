package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.jwt.JwtUtil;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.RefreshTokenRepository;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private S3Uploader s3Uploader;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유효한 요청이 오면 회원가입에 성공한다.")
    void saveUser_withValidRequest_shouldSaveUser() throws IOException {
        // given
        String nickname = "nickname";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String storedFileName = "storedFileName";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test content".getBytes()
        );
        UserSignUpRequest request = new UserSignUpRequest(nickname, password, mockFile);
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(s3Uploader.upload(any(), anyString())).thenReturn(storedFileName);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(User.of(nickname, encodedPassword, storedFileName));

        // when
        UserSignUpResponse response = userService.saveUser(request);

        // then
        assertNotNull(response);
        assertEquals(nickname, response.nickname());
        assertEquals(storedFileName, response.profileImageUrl());
    }

}