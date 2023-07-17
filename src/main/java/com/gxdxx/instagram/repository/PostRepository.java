package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("SELECT MAX(p.id) FROM Post p")
    Optional<Long> findMaxPostId();

}
