package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class UserCreateService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponse createUser(UserSignUpRequest request) {
        checkNicknameDuplication(request.nickname());
        String profileImageUrl = uploadProfileImage(request.profileImage());
        User savedUser = saveUser(request.nickname(), request.password(), profileImageUrl);
        return UserSignUpResponse.of(savedUser);
    }

    private void checkNicknameDuplication(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new NicknameAlreadyExistsException();
        }
    }

    private String uploadProfileImage(MultipartFile image) {
        return s3Uploader.upload(image, "images");
    }

    private User saveUser(String nickname, String password, String profileImageUrl) {
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.save(User.of(nickname, encodedPassword, profileImageUrl));
    }

}
