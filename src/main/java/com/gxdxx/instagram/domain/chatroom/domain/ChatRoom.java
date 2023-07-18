package com.gxdxx.instagram.domain.chatroom.domain;

import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_a_id", nullable = false)
    private User userA;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_b_id", nullable = false)
    private User userB;

    private String lastMessage;

    private LocalDateTime lastSentAt;

    private boolean deleted = Boolean.FALSE;

    private ChatRoom(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }

    public static ChatRoom of(User userA, User userB) {
        return new ChatRoom(userA, userB);
    }

    public void updateLastMessage(String lastMessage, LocalDateTime lastSentAt) {
        this.lastMessage = lastMessage;
        this.lastSentAt = lastSentAt;
    }

    public boolean hasUser(User requestUser) {
        return this.getUserA().equals(requestUser) ||
                this.getUserB().equals(requestUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom chatRoom)) return false;
        return id != null && id.equals(chatRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
