package com.gxdxx.instagram.domain.reply.dao;

import com.gxdxx.instagram.domain.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
