package com.gxdxx.instagram.service;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.RefreshTokenInvalidException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    // 전달받은 RefeshToken으로 토큰 유효성 검사를 진행하고, 유효한 토큰일 때 Refresh Token으로 사용자 id를 찾음
    public SuccessResponse createNewAccessToken(String refreshToken, HttpServletResponse response) {

        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 새로운 Access Token 생성
        String newAccessToken = tokenProvider.createToken(user, TokenProvider.ACCESS_TOKEN);

        // 헤더에 Access Token 추가
        tokenProvider.setHeader(response, newAccessToken);

        return SuccessResponse.of("200 SUCCESS");
    }

}
