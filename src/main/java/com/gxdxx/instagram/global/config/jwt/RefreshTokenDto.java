package com.gxdxx.instagram.global.config.jwt;

public record RefreshTokenDto(

        String refreshToken,

        long expiration

) {
}
