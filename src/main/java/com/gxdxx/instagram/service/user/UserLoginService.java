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
        // 아이디 검사
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new UserNotFoundException());

        // 비밀번호 검사
        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        // 아이디 정보로 Token 생성
        String newAccessToken = tokenProvider.createToken(user, TokenProvider.ACCESS_TOKEN);
        String newRefreshToken = tokenProvider.createToken(user, TokenProvider.REFRESH_TOKEN);

        // Refresh 토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(user.getId());

        // 있다면 새 토큰으로 업데이트
        // 없다면 새로 만들고 DB 저장
        if (refreshToken.isPresent()) {
            refreshToken.get().updateToken(newRefreshToken);
        } else {
            refreshTokenRepository.save(RefreshToken.of(user.getId(), newRefreshToken));
        }

        // response 헤더에 Access Token 넣음
        tokenProvider.setHeader(response, newAccessToken);
        // cookie에 Refresh Token 넣음
        tokenProvider.addCookie(response, newRefreshToken);

        return new SuccessResponse("토큰이 발급되었습니다.");
    }

}
