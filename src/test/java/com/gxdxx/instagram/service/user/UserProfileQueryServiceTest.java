package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.domain.user.application.UserProfileQueryService;
import com.gxdxx.instagram.domain.user.dto.response.UserProfileResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.follow.dao.FollowRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
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

    private static final String NICKNAME = "nickname";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String STORED_FILE_URL = "storedFileUrl";
    private static final Long FOLLOWER_COUNT = 10L;
    private static final Long FOLLOWING_COUNT = 10L;

    @Test
    @DisplayName("[프로필 조회] - 성공")
    void getProfile_shouldSucceed() {
        // given
        User user = createUser();

        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.of(user));
        when(followRepository.countByFollowing(user)).thenReturn(FOLLOWER_COUNT);
        when(followRepository.countByFollower(user)).thenReturn(FOLLOWING_COUNT);

        // when
        UserProfileResponse response = userProfileQueryService.findUserProfile(user.getNickname());

        // then
        assertEquals(user.getNickname(), response.nickname());
        assertEquals(user.getProfileImageUrl(), response.profileImageUrl());
        assertEquals(FOLLOWING_COUNT, response.following());
        assertEquals(FOLLOWER_COUNT, response.follower());
    }

    @Test
    @DisplayName("[프로필 조회] - 실패 (존재하지 않는 유저)")
    public void getProfile_withNonExistingUser_shouldThrowUserNotFoundException() {
        // given
        when(userRepository.findByNickname(NICKNAME)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> userProfileQueryService.findUserProfile(NICKNAME));
    }

    private User createUser() {
        return User.of(NICKNAME, ENCODED_PASSWORD, STORED_FILE_URL);
    }

}
