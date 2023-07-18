package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("[회원 탈퇴] - 성공")
    void deleteUser_success() {
        String nickname = "nickname";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String storedFileName = "storedFileName";
        User deleteUser = User.of(nickname, encodedPassword, storedFileName);
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(deleteUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        SuccessResponse response = userDeleteService.deleteUser(password, nickname);

        assertEquals("회원탈퇴를 성공했습니다.", response.successMessage());
        verify(userRepository, times(1)).findByNickname(nickname);
        verify(userRepository, times(1)).delete(deleteUser);
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (존재하지 않는 회원)")
    void deleteUser_userNotFound() {
        String nickname = "nickname";
        String password = "password";
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userDeleteService.deleteUser(password, nickname));
        verify(userRepository, times(1)).findByNickname(nickname);
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (비밀번호 불일치)")
    void deleteUser_authorizationFailed() {
        String nickname = "nickname";
        String wrongPassword = "wrongPassword";
        String encodedPassword = "encodedPassword";
        String storedFileName = "storedFileName";
        User deleteUser = User.of(nickname, encodedPassword, storedFileName);
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(deleteUser));
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userDeleteService.deleteUser(wrongPassword, nickname));
        verify(userRepository, times(1)).findByNickname(nickname);
        verify(userRepository, never()).delete(any());
    }

}
