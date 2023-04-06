package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.FollowCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.FollowAlreadyExistsException;
import com.gxdxx.instagram.exception.InvalidRequestException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public SuccessResponse createFollow(FollowCreateRequest request, String followerNickname) {

        User follower = userRepository.findByNickname(followerNickname)
                .orElseThrow(UserNotFoundException::new);
        User following = userRepository.findById(request.userId())
                .orElseThrow(UserNotFoundException::new);

        if (follower.getId() == following.getId()) {
            throw new InvalidRequestException();
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new FollowAlreadyExistsException();
        }

        followRepository.save(Follow.createFollow(follower, following));
        return SuccessResponse.of("200 SUCCESS");
    }

}
