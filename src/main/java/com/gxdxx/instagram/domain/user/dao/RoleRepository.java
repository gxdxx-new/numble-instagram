package com.gxdxx.instagram.domain.user.dao;

import com.gxdxx.instagram.domain.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
