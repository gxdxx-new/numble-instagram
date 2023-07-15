package com.gxdxx.instagram.config.jwt;

public record RefreshTokenDto(

        String refreshToken,

        long expiration

) {
}
