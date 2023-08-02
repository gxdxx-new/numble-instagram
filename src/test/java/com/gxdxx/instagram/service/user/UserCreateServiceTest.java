package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.domain.user.application.UserCreateService;
import com.gxdxx.instagram.domain.user.dao.RoleRepository;
import com.gxdxx.instagram.domain.user.domain.Role;
import com.gxdxx.instagram.domain.user.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.domain.user.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.domain.user.exception.RoleNotFoundException;
import com.gxdxx.instagram.global.config.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCreateServiceTest {

    @InjectMocks
    private UserCreateService userCreateService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Uploader s3Uploader;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private static final String NICKNAME = "nickname";
    private static final String EXISTING_NICKNAME = "existingNickname";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String STORED_FILE_URL = "storedFileUrl";
    private static final String ROLE_NAME = "ROLE_USER";
    private static final String ROLE_DESCRIPTION = "사용자";
    private static final String DIR_NAME = "images";

    @Test
    @DisplayName("[회원가입] - 성공")
    void saveUser_withValidRequest_shouldSaveUser() {
        // given
        UserSignUpRequest request = createUserSignUpRequest(NICKNAME, PASSWORD);
        Role userRole = createRole(ROLE_NAME, ROLE_DESCRIPTION);

        when(userRepository.findByNickname(request.nickname()))
                .thenReturn(Optional.empty());
        when(s3Uploader.upload(request.profileImage(), DIR_NAME))
                .thenReturn(STORED_FILE_URL);
        when(passwordEncoder.encode(request.password()))
                .thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class)))
                .thenReturn(User.of(request.nickname(), ENCODED_PASSWORD, STORED_FILE_URL));
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.of(userRole));

        // when
        UserSignUpResponse response = userCreateService.createUser(request);

        // then
        assertEquals(request.nickname(), response.nickname());
        assertEquals(STORED_FILE_URL, response.profileImageUrl());
    }

    @Test
    @DisplayName("[회원가입] - 실패 (이미 존재하는 닉네임)")
    void saveUser_withExistingNickname_shouldThrowException() {
        // given
        UserSignUpRequest request = createUserSignUpRequest(EXISTING_NICKNAME, PASSWORD);
        User existingUser = User.of(EXISTING_NICKNAME, ENCODED_PASSWORD, STORED_FILE_URL);

        when(userRepository.findByNickname(request.nickname())).thenReturn(Optional.of(existingUser));

        // when & then
        assertThrows(NicknameAlreadyExistsException.class, () -> userCreateService.createUser(request));
    }

    private UserSignUpRequest createUserSignUpRequest(String nickname, String password) {
        MockMultipartFile mockFile = getMockMultipartFile();
        return new UserSignUpRequest(nickname, password, mockFile);
    }

    private Role createRole(String name, String description) {
        return Role.of(name, description);
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
