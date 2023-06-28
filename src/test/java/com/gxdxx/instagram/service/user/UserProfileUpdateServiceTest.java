package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import com.gxdxx.instagram.service.S3Uploader;
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
public class UserProfileUpdateServiceTest {

    @InjectMocks
    private UserProfileUpdateService userProfileUpdateService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Uploader s3Uploader;

    @Test
    @DisplayName("[프로필 수정] - 성공")
    void updateProfile_shouldSucceed() {
        User user = createUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest();
        String updateProfileImageUrl = "updateProfileImageUrl";
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        when(s3Uploader.upload(any(), anyString())).thenReturn(updateProfileImageUrl);

        UserProfileUpdateResponse response = userProfileUpdateService.updateUserProfile(request, user.getNickname());

        assertEquals(request.nickname(), response.nickname());
        assertEquals(updateProfileImageUrl, response.profileImageUrl());
    }

    @Test
    @DisplayName("[프로필 수정] - 실패 (존재하지 않는 유저)")
    void updateProfile_withNonExistingUser_shouldThrowUserNotFoundException() {
        User user = createUser();
        UserProfileUpdateRequest request = createUserProfileUpdateRequest();
        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileUpdateService.updateUserProfile(request, user.getNickname()));
    }

    private User createUser() {
        return User.of("nickname", "encodedPassword", "profileImageUrl");
    }

    private UserProfileUpdateRequest createUserProfileUpdateRequest() {
        MockMultipartFile mockFile = getMockMultipartFile();
        String updateNickname = "updateNickname";
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
