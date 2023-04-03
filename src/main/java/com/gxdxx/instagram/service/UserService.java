package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserSignUpResponse saveUser(UserSignUpRequest request) {
        userRepository.findByNickname(request.nickname()).ifPresent(user -> {
            throw new NicknameAlreadyExistsException();
        });
        
        // TODO: 파일업로드 구현하기
        String profileImageUrl = new StringBuffer()
                .append("http://example.com/images/.jpg")
                .insert(26, request.nickname())
                .toString();

        User saveUser = User.of(request.nickname(), profileImageUrl);
        return UserSignUpResponse.of(userRepository.save(saveUser));
    }

    public SuccessResponse deleteUser(Long id) {
        // TODO: 본인이 맞는지 확인
        User deleteUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.delete(deleteUser);
        return SuccessResponse.of("회원탈퇴를 성공했습니다.");
    }

}
