package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
