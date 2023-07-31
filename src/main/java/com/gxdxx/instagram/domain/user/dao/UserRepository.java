package com.gxdxx.instagram.domain.user.dao;

import com.gxdxx.instagram.domain.admin.dto.response.AdminUserListResponse;
import com.gxdxx.instagram.domain.admin.dto.response.AdminUserQueryResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userAuthorities " +
            "WHERE u.nickname = :nickname")
    Optional<User> findByNicknameWithAuthorities(@Param("nickname") String nickname);

    @Query("SELECT new com.gxdxx.instagram.domain.admin.dto.response.AdminUserListResponse(u.id, u.nickname, u.profileImageUrl, u.deleted, u.createdAt, r.name) " +
            "FROM User u " +
            "JOIN u.userRoles ur " +
            "JOIN ur.role r " +
            "WHERE r.name = :roleName " +
            "ORDER BY u.id")
    Page<AdminUserQueryResponse> findUsersByRole(String roleName, Pageable pageable);

}
