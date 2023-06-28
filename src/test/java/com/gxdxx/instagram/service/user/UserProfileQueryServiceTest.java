package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.dto.response.UserProfileResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileQueryServiceTest {

    @InjectMocks
    private UserProfileQueryService userProfileQueryService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowRepository followRepository;

    @Test
    @DisplayName("[프로필 조회] - 성공")
    void getProfile_shouldSucceed() {
        User user = createUser();
        Long followerCount = 10L;
        Long followingCount = 15L;

        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.of(user));
        when(followRepository.countByFollowing(user)).thenReturn(followerCount);
        when(followRepository.countByFollower(user)).thenReturn(followingCount);

        UserProfileResponse response = userProfileQueryService.findUserProfile(user.getNickname());

        assertEquals(user.getNickname(), response.nickname());
        assertEquals(user.getProfileImageUrl(), response.profileImageUrl());
        assertEquals(followingCount, response.following());
        assertEquals(followerCount, response.follower());
    }

    @Test
    @DisplayName("[프로필 조회] - 실패 (존재하지 않는 유저)")
    public void getProfile_withNonExistingUser_shouldThrowUserNotFoundException() {
        User user = createUser();

        when(userRepository.findByNickname(user.getNickname())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileQueryService.findUserProfile(user.getNickname()));
    }

    private User createUser() {
        return User.of("nickname", "encodedPassword", "profileImageUrl");
    }

}
