package com.gxdxx.instagram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String nickname;

    private RefreshToken(String token, String nickname) {
        this.refreshToken = token;
        this.nickname = nickname;
    }

    public static RefreshToken of(String token, String nickname) {
        return new RefreshToken(token, nickname);
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }

}
