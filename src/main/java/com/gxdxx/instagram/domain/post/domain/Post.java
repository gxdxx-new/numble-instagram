package com.gxdxx.instagram.domain.post.domain;

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

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 100)
    @NotBlank
    @Column(nullable = false, length = 100)
    private String content;

    @NotBlank
    @Column(nullable = false)
    private String imageUrl;

    private boolean deleted = Boolean.FALSE;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Post(String content, String imageUrl, User user) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
    }

    public static Post of(String content, String imageUrl, User user) {
        return new Post(content, imageUrl, user);
    }

    public void update(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return id != null && id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
