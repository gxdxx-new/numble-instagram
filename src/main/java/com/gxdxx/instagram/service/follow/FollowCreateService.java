package com.gxdxx.instagram.service.follow;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.FollowAlreadyExistsException;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class FollowCreateService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public SuccessResponse createFollow(FollowCreateRequest request, String followerNickname) {
        User follower = findUserByNickname(followerNickname);
        User following = findUserById(request.userId());
        checkFollowerAndFollowing(follower, following);
        Follow follow = findOrElseCreateFollow(follower, following);
        follow.changeDeleted(false);
        followRepository.save(follow);
        return SuccessResponse.of("200 SUCCESS");
    }

    public User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }


    private void checkFollowerAndFollowing(User follower, User following) {
        if (isSameUser(follower, following)) {
            throw new InvalidRequestException();
        }
        if (isFollowExists(follower, following)) {
            throw new FollowAlreadyExistsException();
        }
    }

    private boolean isSameUser(User user1, User user2) {
        return user1.equals(user2);
    }

    private boolean isFollowExists(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    private Follow findOrElseCreateFollow(User follower, User following) {
        return followRepository.findByFollowerAndFollowingAndDeleted(follower, following, true)
                .orElseGet(() -> Follow.createFollow(follower, following));
    }

}
