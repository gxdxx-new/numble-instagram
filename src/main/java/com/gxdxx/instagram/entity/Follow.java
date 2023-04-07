package com.gxdxx.instagram.entity;

import jakarta.persistence.*;
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
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
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


}
