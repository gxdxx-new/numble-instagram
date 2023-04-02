package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
