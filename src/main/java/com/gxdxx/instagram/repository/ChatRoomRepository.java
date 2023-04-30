package com.gxdxx.instagram.repository;

import com.gxdxx.instagram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT cr FROM ChatRoom cr " +
            "WHERE (cr.userA.id = :senderId AND cr.userB.id = :receiverId) " +
            "OR (cr.userA.id = :receiverId AND cr.userB.id = :senderId)")
    Optional<ChatRoom> findByUserIds(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}
