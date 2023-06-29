package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileUpdateServiceTest {

    @InjectMocks
    private UserProfileUpdateService userProfileUpdateService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("[프로필 수정] - 성공 (기존 닉네임과 같은 경우)")
    void updateUserProfile_WithSameNickname_ShouldSucceed() {
        String newProfileImageUrl = "newProfileImageUrl";
        User savedUser = createSavedUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest(savedUser.getNickname());
        when(userRepository.findByNickname(savedUser.getNickname())).thenReturn(Optional.of(savedUser));
        when(s3Uploader.upload(any(), anyString())).thenReturn(newProfileImageUrl);

        UserProfileUpdateResponse response = userProfileUpdateService.updateUserProfile(request, savedUser.getNickname());

        assertEquals(request.nickname(), response.nickname());
        assertEquals(newProfileImageUrl, response.profileImageUrl());
    }

    @Test
    @DisplayName("[프로필 수정] - 성공 (기존 닉네임과 다른 경우)")
    void updateUserProfile_WithDifferentNickname_ShouldSucceed() {
        String newNickname = "newNickname";
        String newProfileImageUrl = "newProfileImageUrl";
        User savedUser = createSavedUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest(newNickname);
        when(userRepository.findByNickname(request.nickname())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(savedUser.getNickname())).thenReturn(Optional.of(savedUser));
        when(s3Uploader.upload(any(), anyString())).thenReturn(newProfileImageUrl);

        UserProfileUpdateResponse response = userProfileUpdateService.updateUserProfile(request, savedUser.getNickname());

        assertEquals(request.nickname(), response.nickname());
        assertEquals(newProfileImageUrl, response.profileImageUrl());
    }

    @Test
    @DisplayName("[프로필 수정] - 실패 (이미 존재하는 닉네임)")
    void updateUserProfile_withExistingNickname_shouldThrowException() {
        String newNickname = "newNickname";
        User duplicatedUser = createDuplicatedUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest(newNickname);
        when(userRepository.findByNickname(request.nickname())).thenReturn(Optional.of(duplicatedUser));

        Assertions.assertThrows(NicknameAlreadyExistsException.class, () -> userProfileUpdateService.updateUserProfile(request, duplicatedUser.getNickname()));
    }

    @Test
    @DisplayName("[프로필 수정] - 실패 (존재하지 않는 유저 && 기존 닉네임과 같은 경우)")
    void updateUserProfile_withNonExistingUser_withSameNickname_shouldThrowUserNotFoundException() {
        User savedUser = createSavedUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest(savedUser.getNickname());
        when(userRepository.findByNickname(savedUser.getNickname())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileUpdateService.updateUserProfile(request, savedUser.getNickname()));
    }

    @Test
    @DisplayName("[프로필 수정] - 실패 (존재하지 않는 유저 && 기존 닉네임과 다른 경우)")
    void updateUserProfile_withNonExistingUser_withDifferentNickname_shouldThrowUserNotFoundException() {
        String newNickname = "newNickname";
        User user = createSavedUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest(newNickname);
        when(userRepository.findByNickname(request.nickname())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileUpdateService.updateUserProfile(request, user.getNickname()));
    }

    private User createSavedUser() {
        return User.of("savedNickname", "encodedPassword", "profileImageUrl");
    }

    private User createDuplicatedUser() {
        return User.of("duplicatedNickname", "encodedPassword", "profileImageUrl");
    }

    private UserProfileUpdateRequest createUserProfileUpdateRequest(String nickname) {
        String updateNickname = nickname;
        MockMultipartFile mockFile = getMockMultipartFile();
        return new UserProfileUpdateRequest(updateNickname, mockFile);
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
