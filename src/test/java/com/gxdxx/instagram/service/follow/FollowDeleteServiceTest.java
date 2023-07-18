package com.gxdxx.instagram.service.follow;

import com.gxdxx.instagram.domain.follow.application.FollowDeleteService;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.follow.domain.Follow;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.follow.exception.FollowNotFountException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.follow.dao.FollowRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowDeleteServiceTest {

    @InjectMocks
    private FollowDeleteService followDeleteService;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;

    private User follower;
    private User following;

    @BeforeEach
    public void setUp() {
        follower = User.of("Follower", "password", "follower@example.com");
        following = User.of("Following", "password", "following@example.com");
    }

    @Test
    @DisplayName("[팔로우 취소] - 성공")
    public void deleteFollow_shouldSucceed() {
        Follow follow = Follow.createFollow(follower, following);

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.findByFollowerAndFollowing(follower, following)).thenReturn(Optional.of(follow));
        doNothing().when(followRepository).delete(follow);

        SuccessResponse response = followDeleteService.deleteFollow(following.getId(), follower.getNickname());

        Assertions.assertEquals("200 SUCCESS", response.successMessage());
    }

    @Test
    @DisplayName("[팔로우 취소] - 실패 (존재하지 않는 팔로우 관계)")
    public void deleteFollow_withNonExistingFollow_shouldThrowFollowNotFoundException() {

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.findByFollowerAndFollowing(follower, following)).thenReturn(Optional.empty());

        Assertions.assertThrows(FollowNotFountException.class, () -> followDeleteService.deleteFollow(following.getId(), follower.getNickname()));
    }

    @Test
    @DisplayName("[팔로우 취소] - 실패 (요청자 닉네임에 해당하는 유저가 존재하지 않는 경우)")
    public void deleteFollow_withNonExistingUser_shouldThrowUserNotFoundException() {
        String nonExistingNickname = "non-existing-nickname";

        when(userRepository.findByNickname(nonExistingNickname)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> followDeleteService.deleteFollow(following.getId(), nonExistingNickname));
    }

}
