package com.gxdxx.instagram.domain.follow.domain;

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

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE follow SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    private boolean deleted = Boolean.FALSE;

    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public static Follow createFollow(User follower, User following) {
        return new Follow(follower, following);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Follow follow)) return false;
        return id != null && id.equals(follow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void changeDeleted(boolean status) {
        this.deleted = status;
    }

}
