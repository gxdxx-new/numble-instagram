package com.gxdxx.instagram.domain.follow.application;

import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.follow.domain.Follow;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.follow.exception.FollowNotFountException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.follow.dao.FollowRepository;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class FollowDeleteService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public SuccessResponse deleteFollow(Long followingId, String followerNickname) {
        User follower = findUserByNickname(followerNickname);
        User following = findUserById(followingId);
        Follow follow = findFollowByFollowerAndFollowing(follower, following);
        followRepository.delete(follow);
        return SuccessResponse.of("200 SUCCESS");
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private Follow findFollowByFollowerAndFollowing(User follower, User following) {
        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(FollowNotFountException::new);
    }

}
