package com.gxdxx.instagram.domain.user.application;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.global.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.global.auth.RefreshTokenInvalidException;
import com.gxdxx.instagram.domain.user.exception.UserNotFoundException;
import com.gxdxx.instagram.domain.user.dao.UserRepository;
import com.gxdxx.instagram.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.gxdxx.instagram.global.redis.RedisService.ACCESS_TOKEN_BLACKLIST_VALUE;
import static com.gxdxx.instagram.global.redis.RedisService.REFRESH_TOKEN_KEY_PREFIX;

@Transactional
@RequiredArgsConstructor
@Service
public class UserLogoutService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    public SuccessResponse logout(String refreshToken, String authorizationHeader, String nickname) {
        // AT 값에서 접두사 제거
        String accessToken = removePrefixFromAccessToken(authorizationHeader);
        // 로그아웃은 필터에서 AT가 검증되고 넘어올것임.
        // RT 검증
        validateRefreshToken(refreshToken);
        User userToLogout = findUserByNickname(nickname);
        // RT 삭제
        deleteRefreshToken(userToLogout);
        // AT blacklist 등록
        addAccessTokenToBlacklist(accessToken);
        return SuccessResponse.of("200 SUCCESS");
    }

    private String removePrefixFromAccessToken(String authorizationHeader) {
        return tokenProvider.getAccessToken(authorizationHeader);
    }

    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private void deleteRefreshToken(User user) {
        redisService.deleteValues(REFRESH_TOKEN_KEY_PREFIX + user.getId());
    }

    private void addAccessTokenToBlacklist(String accessToken) {
        redisService.setValues(
                accessToken,
                ACCESS_TOKEN_BLACKLIST_VALUE,
                tokenProvider.getExpiration(accessToken) - new Date().getTime()
        );
    }

}
