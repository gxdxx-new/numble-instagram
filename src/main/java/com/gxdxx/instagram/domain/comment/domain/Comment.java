package com.gxdxx.instagram.domain.comment.domain;

import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.domain.BaseEntity;
import com.gxdxx.instagram.domain.post.domain.Post;
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

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100)
    @NotBlank
    @Column(nullable = false, length = 100)
    private String content;

    private boolean deleted = Boolean.FALSE;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public static Comment of(String content, User user, Post post) {
        return new Comment(content, user, post);
    }

    public void update(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return id != null && id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
