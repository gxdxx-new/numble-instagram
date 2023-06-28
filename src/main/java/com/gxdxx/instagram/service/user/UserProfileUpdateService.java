package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import com.gxdxx.instagram.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserProfileUpdateService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public UserProfileUpdateResponse updateUserProfile(UserProfileUpdateRequest request, String nickname) {
        User user = getUserFromNickname(nickname);

        String profileImageUrl =  s3Uploader.upload(request.profileImage(), "images");

        user.updateProfile(request.nickname(), profileImageUrl);
        return UserProfileUpdateResponse.of(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

    private User getUserFromNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

}
