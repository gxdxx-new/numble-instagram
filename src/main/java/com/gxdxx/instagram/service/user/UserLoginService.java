package com.gxdxx.instagram.service.user;

import com.gxdxx.instagram.config.jwt.TokenProvider;
import com.gxdxx.instagram.dto.request.UserLoginRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.entity.RefreshToken;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.repository.RefreshTokenRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public SuccessResponse login(UserLoginRequest request, HttpServletResponse response) {
        User savedUser = findUserByNickname(request.nickname());
        checkPasswordMatches(request.password(), savedUser.getPassword());
        String newAccessToken = createAccessToken(savedUser);
        String newRefreshToken = createRefreshToken(savedUser);
        updateOrCreateRefreshToken(savedUser.getId(), newRefreshToken);
        setAccessTokenHeader(response, newAccessToken);
        addRefreshTokenCookie(response, newRefreshToken);
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
        return tokenProvider.createToken(user, TokenProvider.ACCESS_TOKEN);
    }

    private String createRefreshToken(User user) {
        return tokenProvider.createToken(user, TokenProvider.REFRESH_TOKEN);
    }

    private void updateOrCreateRefreshToken(Long userId, String newRefreshToken) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(userId);
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(newRefreshToken);
            return;
        }
        refreshTokenRepository.save(RefreshToken.of(userId, newRefreshToken));
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        tokenProvider.setHeader(response, accessToken);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        tokenProvider.addCookie(response, refreshToken);
    }

}
