package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.dto.response.UserProfileResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.follow.dao.FollowRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserProfileQueryService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse findUserProfile(String nickname) {
        User userToViewProfile = findUserByNickname(nickname);
        Long followerCount = getFollowerCount(userToViewProfile);
        Long followingCount = getFollowingCount(userToViewProfile);
        return createProfileResponse(userToViewProfile, followerCount, followingCount);
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private Long getFollowerCount(User user) {
        return followRepository.countByFollowing(user);
    }

    private Long getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }

    private UserProfileResponse createProfileResponse(User user, Long followerCount, Long followingCount) {
        return UserProfileResponse.of(user.getNickname(), user.getProfileImageUrl(), followerCount, followingCount);
    }

}
