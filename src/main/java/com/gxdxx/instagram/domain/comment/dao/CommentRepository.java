package com.gxdxx.instagram.domain.comment.dao;

import com.gxdxx.instagram.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
