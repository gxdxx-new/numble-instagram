package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDeleteServiceTest {

    @InjectMocks
    private UserDeleteService userDeleteService;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("[회원 탈퇴] - 성공")
    void deleteUser_success() {
        Long userId = 1L;
        String nickname = "nickname";
        String encodedPassword = "encodedPassword";
        String storedFileName = "storedFileName";
        User deleteUser = User.of(nickname, encodedPassword, storedFileName);
        when(userRepository.findById(userId)).thenReturn(Optional.of(deleteUser));

        SuccessResponse response = userDeleteService.deleteUser(userId, nickname);

        assertEquals("회원탈퇴를 성공했습니다.", response.successMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(deleteUser);
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (존재하지 않는 회원)")
    void deleteUser_userNotFound() {
        Long userId = 1L;
        String nickname = "nickname";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userDeleteService.deleteUser(userId, nickname));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (인가되지 않은 회원)")
    void deleteUser_authorizationFailed() {
        Long userId = 1L;
        String nickname = "nickname";
        String wrongNickname = "wrongNickname";
        String encodedPassword = "encodedPassword";
        String storedFileName = "storedFileName";
        User deleteUser = User.of(wrongNickname, encodedPassword, storedFileName);

        when(userRepository.findById(userId)).thenReturn(Optional.of(deleteUser));

        assertThrows(UnauthorizedAccessException.class, () -> userDeleteService.deleteUser(userId, nickname));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any());
    }

}
