package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    private User follower;
    private User following;

    @BeforeEach
    public void setUp() {
        follower = User.of("Follower", "password", "follower@example.com");
        following = User.of("Following", "password", "following@example.com");
    }

    @Test
    @DisplayName("[팔로우] - 성공 (soft delete되었던 팔로우)")
    public void createFollow_withSoftDeletedFollow_shouldCreateFollow() {
        FollowCreateRequest request = new FollowCreateRequest(following.getId());
        Follow softDeletedFollow = Follow.createFollow(follower, following);
        softDeletedFollow.changeDeleted(true);

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following)).thenReturn(false);
        when(followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true)).thenReturn(Optional.of(softDeletedFollow));
        when(followRepository.save(softDeletedFollow)).thenReturn(softDeletedFollow);

        SuccessResponse response = followService.createFollow(request, follower.getNickname());
        Assertions.assertEquals("200 SUCCESS", response.message());
    }

}