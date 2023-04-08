package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
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
