package com.gxdxx.instagram.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reply SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100)
    @NotBlank
    @Column(nullable = false, length = 100)
    private String content;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private Reply(String content, User user, Comment comment) {
        this.content = content;
        this.user = user;
        this.comment = comment;
    }

    public static Reply of(String content, User user, Comment comment) {
        return new Reply(content, user, comment);
    }

    public void update(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reply reply)) return false;
        return id != null && id.equals(reply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
