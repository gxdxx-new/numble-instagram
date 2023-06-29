package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.request.UserDeleteRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
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
        User deleteUser = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(password, deleteUser.getPassword())) {
            throw new PasswordNotMatchException();
        }
        userRepository.delete(deleteUser);
        return SuccessResponse.of("회원탈퇴를 성공했습니다.");
    }

}
