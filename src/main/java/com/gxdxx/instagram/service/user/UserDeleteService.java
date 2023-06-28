package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserDeleteService {

    private final UserRepository userRepository;

    public SuccessResponse deleteUser(Long id, String nickname) {
        User deleteUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!deleteUser.getNickname().equals(nickname)) {
            throw new UnauthorizedAccessException();
        }
        userRepository.delete(deleteUser);
        return SuccessResponse.of("회원탈퇴를 성공했습니다.");
    }

}
