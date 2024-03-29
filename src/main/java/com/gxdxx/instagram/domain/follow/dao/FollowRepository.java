package com.gxdxx.instagram.domain.follow.dao;

import com.gxdxx.instagram.domain.follow.domain.Follow;
import com.gxdxx.instagram.domain.user.domain.User;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    @Where(clause = "")
    Optional<Follow> findByFollowerAndFollowingAndDeleted(User follower, User following, boolean status);

    Long countByFollower(User follower);

    Long countByFollowing(User following);

}
