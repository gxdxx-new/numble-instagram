package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.domain.user.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
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
        checkNicknameEquality(request.nickname(), nickname);
        User userToUpdate = findUserByNickname(nickname);
        String newProfileImageUrl = uploadProfileImage(request.profileImage());
        updateProfile(userToUpdate, request.nickname(), newProfileImageUrl);
        return createUserProfileUpdateResponse(userToUpdate);
    }

    private void checkNicknameEquality(String newNickname, String savedNickname) {
        if (!newNickname.equals(savedNickname)) {
            checkNicknameDuplication(newNickname);
        }
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

    private UserProfileUpdateResponse createUserProfileUpdateResponse(User user) {
        return UserProfileUpdateResponse.of(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

}
