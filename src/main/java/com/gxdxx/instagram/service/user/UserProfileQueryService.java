package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.UserProfileResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
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
        User user = findUserByNickname(nickname);
        Long followerCount = getFollowerCount(user);
        Long followingCount = getFollowingCount(user);
        return createProfileResponse(user, followerCount, followingCount);
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
