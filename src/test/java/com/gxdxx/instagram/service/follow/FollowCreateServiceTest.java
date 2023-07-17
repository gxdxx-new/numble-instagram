package com.gxdxx.instagram.service.follow;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.follow.domain.Follow;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.FollowAlreadyExistsException;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UserNotFoundException;
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

    @BeforeEach
    public void setUp() {
        follower = User.of("Follower", "password", "follower@example.com");
        following = User.of("Following", "password", "following@example.com");
    }

    @Test
    @DisplayName("[팔로우] - 성공 (soft delete되었던 팔로우)")
    public void createFollow_withSoftDeletedFollow_shouldSucceed() {
        FollowCreateRequest request = new FollowCreateRequest(following.getId());
        Follow softDeletedFollow = Follow.createFollow(follower, following);
        softDeletedFollow.changeDeleted(true);

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following)).thenReturn(false);
        when(followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true)).thenReturn(Optional.of(softDeletedFollow));
        when(followRepository.save(softDeletedFollow)).thenReturn(softDeletedFollow);

        SuccessResponse response = followCreateService.createFollow(request, follower.getNickname());
        Assertions.assertEquals("200 SUCCESS", response.successMessage());
    }

    @Test
    @DisplayName("[팔로우] - 성공 (soft delete되었던 적 없는 팔로우)")
    public void createFollow_withoutSoftDeletedFollow_shouldSucceed() {
        FollowCreateRequest request = new FollowCreateRequest(following.getId());
        Follow newFollow = Follow.createFollow(follower, following);

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following)).thenReturn(false);
        when(followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true)).thenReturn(Optional.empty());
        when(followRepository.save(any())).thenReturn(newFollow);

        SuccessResponse response = followCreateService.createFollow(request, follower.getNickname());
        Assertions.assertEquals("200 SUCCESS", response.successMessage());
    }

    @Test
    @DisplayName("[팔로우] - 실패 (요청자와 팔로우할 유저가 같을 경우)")
    public void createFollow_withSameUser_shouldThrowInvalidRequestException() {
        FollowCreateRequest request = new FollowCreateRequest(follower.getId());

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(follower.getId())).thenReturn(Optional.of(follower));

        Assertions.assertThrows(InvalidRequestException.class, () -> followCreateService.createFollow(request, follower.getNickname()));
    }

    @Test
    @DisplayName("[팔로우] - 실패 (이미 존재하는 팔로우 관계일 경우)")
    public void createFollow_withAlreadyExistsFollow_shouldThrowFollowAlreadyExistsException() {
        FollowCreateRequest request = new FollowCreateRequest(following.getId());

        when(userRepository.findByNickname(follower.getNickname())).thenReturn(Optional.of(follower));
        when(userRepository.findById(following.getId())).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following)).thenReturn(true);

        Assertions.assertThrows(FollowAlreadyExistsException.class, () -> followCreateService.createFollow(request, follower.getNickname()));
    }

    @Test
    @DisplayName("[팔로우] - 실패 (요청자 닉네임에 해당하는 유저가 존재하지 않는 경우)")
    public void createFollow_withFollowerNotFound_shouldThrowUserNotFoundException() {
        String nonExistingNickname = "non-existing-nickname";
        FollowCreateRequest request = new FollowCreateRequest(following.getId());

        when(userRepository.findByNickname(nonExistingNickname)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> followCreateService.createFollow(request, nonExistingNickname));
    }

}
