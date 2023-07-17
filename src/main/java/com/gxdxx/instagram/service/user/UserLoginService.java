package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.config.jwt.RefreshTokenDto;
import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.request.UserLoginRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.domain.user.domain.User;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import com.gxdxx.instagram.service.redis.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.gxdxx.instagram.service.redis.RedisService.REFRESH_TOKEN_KEY_PREFIX;

@Transactional
@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    public SuccessResponse login(UserLoginRequest request, HttpServletResponse response) {
        User userToLogin = findUserByNickname(request.nickname());
        checkPasswordMatches(request.password(), userToLogin.getPassword());
        String newAccessToken = createAccessToken(userToLogin);
        RefreshTokenDto newRefreshToken = createRefreshToken();
        saveRefreshTokenInRedis(userToLogin.getId(), newRefreshToken.refreshToken(), newRefreshToken.expiration());
        setAccessTokenHeader(response, newAccessToken);
        addRefreshTokenCookie(response, newRefreshToken.refreshToken());
        return new SuccessResponse("토큰이 발급되었습니다.");
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private void checkPasswordMatches(String password, String storedPassword) {
        if (!passwordEncoder.matches(password, storedPassword)) {
            throw new PasswordNotMatchException();
        }
    }

    private String createAccessToken(User user) {
        return tokenProvider.createAccessToken(user);
    }

    private RefreshTokenDto createRefreshToken() {
        return tokenProvider.createRefreshToken();
    }

    private void saveRefreshTokenInRedis(Long userId, String refreshToken, long expiration) {
        redisService.setValues(
                REFRESH_TOKEN_KEY_PREFIX + userId,
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
