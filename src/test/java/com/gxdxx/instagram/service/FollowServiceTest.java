package com.gxdxx.instagram.service;

import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}