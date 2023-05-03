package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

    @Query("SELECT MAX(m.id) FROM Message m")
    Optional<Long> findMaxMessageId();

}
