package com.gxdxx.instagram.service.token;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.request.AccessTokenCreateRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.RefreshTokenInvalidException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import com.gxdxx.instagram.service.redis.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.gxdxx.instagram.service.redis.RedisService.ACCESS_TOKEN_BLACKLIST_VALUE;

@RequiredArgsConstructor
@Transactional
@Service
public class AccessTokenCreateService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    // 전달받은 RefeshToken으로 토큰 유효성 검사를 진행하고, 유효한 토큰일 때 Refresh Token으로 사용자 id를 찾음
    public SuccessResponse createAccessToken(String refreshToken, String authorizationHeader, AccessTokenCreateRequest request, HttpServletResponse response) {
        validateRefreshToken(refreshToken);
        Long userId = request.userId();
        redisService.checkRefreshToken(userId, refreshToken);
        User userToCreateAccessToken = findUserById(userId);
        // AT가 만료되지 않은 상태에서 AT 재발급 요청을 할 경우 이전 AT를 black list에 추가
        String accessToken = removePrefixFromAccessToken(authorizationHeader);
        validateAndAddAccessTokenToBlacklist(accessToken);
        // 새로운 Access Token 생성
        String newAccessToken = createAccessToken(userToCreateAccessToken);
        // 헤더에 Access Token 추가
        addAccessTokenToHeader(response, newAccessToken);
        return SuccessResponse.of("200 SUCCESS");
    }

    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
    }

    private  User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private String removePrefixFromAccessToken(String authorizationHeader) {
        return tokenProvider.getAccessToken(authorizationHeader);
    }

    private void validateAndAddAccessTokenToBlacklist(String accessToken) {
        if (tokenProvider.validateToken(accessToken)) {
            addAccessTokenToBlacklist(accessToken);
        }
    }

    private void addAccessTokenToBlacklist(String accessToken) {
        redisService.setValues(
                accessToken,
                ACCESS_TOKEN_BLACKLIST_VALUE,
                tokenProvider.getExpiration(accessToken) - new Date().getTime()
        );
    }

    private String createAccessToken(User user) {
        return tokenProvider.createAccessToken(user);
    }

    private void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        tokenProvider.setHeader(response, accessToken);
    }

}
