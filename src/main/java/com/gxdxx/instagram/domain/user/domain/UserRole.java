package com.gxdxx.instagram.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    private UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public static UserRole of(User user, Role role) {
        return new UserRole(user, role);
    }

    public void setUserAndRoleNull() {
        this.user = null;
        this.role = null;
    }

}
