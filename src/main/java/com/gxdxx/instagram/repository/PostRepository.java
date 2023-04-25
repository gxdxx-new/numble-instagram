package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
