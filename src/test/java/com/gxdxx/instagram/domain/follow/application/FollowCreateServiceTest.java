package com.gxdxx.instagram.domain.follow.application;

import com.gxdxx.instagram.domain.follow.application.FollowCreateService;
import com.gxdxx.instagram.domain.follow.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.follow.domain.Follow;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.follow.exception.FollowAlreadyExistsException;
import com.gxdxx.instagram.global.error.InvalidRequestException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowCreateServiceTest {

    @InjectMocks
    private FollowCreateService followCreateService;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;
    private User follower;
    private User following;

    private static final String FOLLOWER = "Follower";
    private static final String FOLLOWING = "Following";
    private static final String PASSWORD = "password";
    private static final String FOLLOWER_PROFILE_IMAGE_URL = "follower@example.com";
    private static final String FOLLOWING_PROFILE_IMAGE_URL = "following@example.com";
    private static final String NON_EXISTING_NICKNAME = "nonExistingNickname";
    private static final String SUCCESS_MESSAGE = "200 SUCCESS";


    @BeforeEach
    public void setUp() {
        follower = User.of(FOLLOWER, PASSWORD, FOLLOWER_PROFILE_IMAGE_URL);
        following = User.of(FOLLOWING, PASSWORD, FOLLOWING_PROFILE_IMAGE_URL);
    }

    @Test
    @DisplayName("[팔로우] - 성공 (soft delete되었던 팔로우)")
    public void createFollow_withSoftDeletedFollow_shouldSucceed() {
        // given
        FollowCreateRequest request = new FollowCreateRequest(following.getId());
        Follow softDeletedFollow = Follow.createFollow(follower, following);
        softDeletedFollow.changeDeleted(true);

        when(userRepository.findByNickname(follower.getNickname()))
                .thenReturn(Optional.of(follower));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following))
                .thenReturn(false);
        when(followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true))
                .thenReturn(Optional.of(softDeletedFollow));
        when(followRepository.save(softDeletedFollow))
                .thenReturn(softDeletedFollow);

        // when
        SuccessResponse response = followCreateService.createFollow(request, follower.getNickname());

        // then
        Assertions.assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[팔로우] - 성공 (soft delete되었던 적 없는 팔로우)")
    public void createFollow_withoutSoftDeletedFollow_shouldSucceed() {
        // given
        FollowCreateRequest request = new FollowCreateRequest(following.getId());
        Follow newFollow = Follow.createFollow(follower, following);

        when(userRepository.findByNickname(follower.getNickname()))
                .thenReturn(Optional.of(follower));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following))
                .thenReturn(false);
        when(followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true))
                .thenReturn(Optional.empty());
        when(followRepository.save(any()))
                .thenReturn(newFollow);

        // when
        SuccessResponse response = followCreateService.createFollow(request, follower.getNickname());

        // then
        Assertions.assertEquals(SUCCESS_MESSAGE, response.successMessage());
    }

    @Test
    @DisplayName("[팔로우] - 실패 (요청자와 팔로우할 유저가 같을 경우)")
    public void createFollow_withSameUser_shouldThrowInvalidRequestException() {
        // given
        FollowCreateRequest request = new FollowCreateRequest(following.getId());

        when(userRepository.findByNickname(follower.getNickname()))
                .thenReturn(Optional.of(follower));
        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(follower));

        // when & then
        Assertions.assertThrows(InvalidRequestException.class,
                () -> followCreateService.createFollow(request, follower.getNickname()));
    }

    @Test
    @DisplayName("[팔로우] - 실패 (이미 존재하는 팔로우 관계일 경우)")
    public void createFollow_withAlreadyExistsFollow_shouldThrowFollowAlreadyExistsException() {
        // given
        FollowCreateRequest request = new FollowCreateRequest(following.getId());

        when(userRepository.findByNickname(follower.getNickname()))
                .thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId()))
                .thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following))
                .thenReturn(true);

        // when & then
        Assertions.assertThrows(FollowAlreadyExistsException.class,
                () -> followCreateService.createFollow(request, follower.getNickname()));
    }

    @Test
    @DisplayName("[팔로우] - 실패 (요청자 닉네임에 해당하는 유저가 존재하지 않는 경우)")
    public void createFollow_withFollowerNotFound_shouldThrowUserNotFoundException() {
        // given
        FollowCreateRequest request = new FollowCreateRequest(following.getId());

        when(userRepository.findByNickname(NON_EXISTING_NICKNAME)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UserNotFoundException.class,
                () -> followCreateService.createFollow(request, NON_EXISTING_NICKNAME));
    }

}
