package com.gxdxx.instagram.domain.message.domain;

import com.gxdxx.instagram.domain.chatroom.domain.ChatRoom;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@SQLDelete(sql = "UPDATE message SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100)
    @NotBlank
    @Column(nullable = false, length = 100)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private LocalDateTime sentAt;

    private boolean deleted = Boolean.FALSE;

    private Message(String content, ChatRoom chatRoom, User sender, User receiver, LocalDateTime sentAt) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = sentAt;
    }

    public static Message of(String content, ChatRoom chatRoom, User sender, User receiver, LocalDateTime sentAt) {
        return new Message(content, chatRoom, sender, receiver, sentAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return id != null && id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
