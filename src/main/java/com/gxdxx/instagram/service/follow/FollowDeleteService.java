package com.gxdxx.instagram.service.follow;

import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.FollowNotFountException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
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
