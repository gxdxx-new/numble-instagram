package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import com.gxdxx.instagram.config.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class UserProfileUpdateService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public UserProfileUpdateResponse updateUserProfile(UserProfileUpdateRequest request, String nickname) {
        checkNicknameDuplication(request.nickname());
        User savedUser = findUserByNickname(nickname);
        String newProfileImageUrl = uploadProfileImage(request.profileImage());
        updateProfile(savedUser, request.nickname(), newProfileImageUrl);
        return createUserProfileUpdateResponse(savedUser);
    }

    private void checkNicknameDuplication(String newNickname) {
        if (userRepository.findByNickname(newNickname).isPresent()) {
            throw new NicknameAlreadyExistsException();
        }
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private String uploadProfileImage(MultipartFile image) {
        return s3Uploader.upload(image, "images");
    }

    private void updateProfile(User user, String newNickname, String newProfileImageUrl) {
        user.updateProfile(newNickname, newProfileImageUrl);
    }

    private UserProfileUpdateResponse createUserProfileUpdateResponse(User savedUser) {
        return UserProfileUpdateResponse.of(savedUser.getId(), savedUser.getNickname(), savedUser.getProfileImageUrl());
    }

}
