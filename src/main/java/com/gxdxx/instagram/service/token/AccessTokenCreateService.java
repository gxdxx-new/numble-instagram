package com.gxdxx.instagram.service.token;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.RefreshTokenInvalidException;
import com.gxdxx.instagram.exception.RefreshTokenNotFoundException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.RefreshTokenRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AccessTokenCreateService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    // 전달받은 RefeshToken으로 토큰 유효성 검사를 진행하고, 유효한 토큰일 때 Refresh Token으로 사용자 id를 찾음
    public SuccessResponse createAccessToken(String refreshToken, HttpServletResponse response) {
        validateRefreshToken(refreshToken);
        Long userId = findUserIdByRefreshToken(refreshToken);
        User user = findUserById(userId);
        // 새로운 Access Token 생성
        String newAccessToken = createAccessToken(user);
        // 헤더에 Access Token 추가
        addAccessTokenToHeader(response, newAccessToken);
        return SuccessResponse.of("200 SUCCESS");
    }

    private void validateRefreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
    }

    private Long findUserIdByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new)
                .getUserId();
    }

    private  User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private String createAccessToken(User user) {
        return tokenProvider.createToken(user, TokenProvider.ACCESS_TOKEN);
    }

    private void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        tokenProvider.setHeader(response, accessToken);
    }

}
