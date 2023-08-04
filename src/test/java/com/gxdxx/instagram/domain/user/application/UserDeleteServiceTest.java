package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.domain.user.application.UserDeleteService;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.PasswordNotMatchException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
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

    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String WRONG_PASSWORD = "wrongPassword";
    private static final String STORED_FILE_URL = "storedFileUrl";
    private static final String SUCCESS_MESSAGE = "회원탈퇴를 성공했습니다.";


    @Test
    @DisplayName("[회원 탈퇴] - 성공")
    void deleteUser_success() {
        User deleteUser = User.of(NICKNAME, ENCODED_PASSWORD, STORED_FILE_URL);

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(deleteUser));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        SuccessResponse response = userDeleteService.deleteUser(PASSWORD, NICKNAME);

        assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (존재하지 않는 회원)")
    void deleteUser_userNotFound() {
        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userDeleteService.deleteUser(PASSWORD, NICKNAME));
        verify(userRepository, never()).delete(any());
    }

    @Test
    @DisplayName("[회원 탈퇴] - 실패 (비밀번호 불일치)")
    void deleteUser_passwordNotMatch() {
        User deleteUser = User.of(NICKNAME, ENCODED_PASSWORD, STORED_FILE_URL);
        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(deleteUser));
        when(passwordEncoder.matches(WRONG_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userDeleteService.deleteUser(WRONG_PASSWORD, NICKNAME));
        verify(userRepository, never()).delete(any());
    }

}
