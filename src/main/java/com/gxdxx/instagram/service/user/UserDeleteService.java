package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserDeleteService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public SuccessResponse deleteUser(String password, String nickname) {
        User userToDelete = findUserByNickname(nickname);
        checkPasswordMatches(password, userToDelete.getPassword());
        deleteUserData(userToDelete);
        return SuccessResponse.of("회원탈퇴를 성공했습니다.");
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private void checkPasswordMatches(String password, String storedPassword) {
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new PasswordNotMatchException();
        }
    }

    private void deleteUserData(User user) {
        userRepository.delete(user);
    }

}
