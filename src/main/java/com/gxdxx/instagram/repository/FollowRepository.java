package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.Follow;
import com.gxdxx.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

}
