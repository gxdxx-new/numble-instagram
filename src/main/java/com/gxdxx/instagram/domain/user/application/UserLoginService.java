package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.global.config.jwt.RefreshTokenDto;
import com.gxdxx.instagram.global.config.jwt.TokenProvider;
import com.gxdxx.instagram.domain.user.dto.request.UserLoginRequest;
import com.gxdxx.instagram.global.common.dto.response.SuccessResponse;
import com.gxdxx.instagram.global.redis.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.gxdxx.instagram.global.redis.RedisService.REFRESH_TOKEN_KEY_PREFIX;

@Transactional
@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public SuccessResponse login(UserLoginRequest request, HttpServletResponse response) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.nickname(), request.password());
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 UserDetailsServiceImpl 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String newAccessToken = createAccessToken(authentication);
        RefreshTokenDto newRefreshToken = createRefreshToken();

        saveRefreshTokenInRedis(authentication.getName(), newRefreshToken.refreshToken(), newRefreshToken.expiration());
        setAccessTokenHeader(response, newAccessToken);
        addRefreshTokenCookie(response, newRefreshToken.refreshToken());
        return new SuccessResponse("토큰이 발급되었습니다.");
    }

    private String createAccessToken(Authentication authentication) {
        return tokenProvider.createAccessToken(authentication);
    }

    private RefreshTokenDto createRefreshToken() {
        return tokenProvider.createRefreshToken();
    }

    private void saveRefreshTokenInRedis(String nickname, String refreshToken, long expiration) {
        redisService.setValues(
                REFRESH_TOKEN_KEY_PREFIX + nickname,
                refreshToken,
                expiration - new Date().getTime()
        );
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        tokenProvider.setHeader(response, accessToken);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        tokenProvider.addCookie(response, refreshToken);
    }

}
