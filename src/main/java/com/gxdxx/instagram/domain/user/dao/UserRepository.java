package com.gxdxx.instagram.domain.user.dao;

import com.gxdxx.instagram.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles " +
            "WHERE u.nickname = :nickname")
    Optional<User> findByNicknameWithRoles(@Param("nickname") String nickname);

}
