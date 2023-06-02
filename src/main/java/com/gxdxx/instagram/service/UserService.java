package com.gxdxx.instagram.service;

import com.gxdxx.instagram.dto.request.UserLoginRequest;
import com.gxdxx.instagram.dto.request.UserProfileUpdateRequest;
import com.gxdxx.instagram.dto.request.UserSignUpRequest;
import com.gxdxx.instagram.dto.response.SuccessResponse;
import com.gxdxx.instagram.dto.response.UserProfileResponse;
import com.gxdxx.instagram.dto.response.UserProfileUpdateResponse;
import com.gxdxx.instagram.dto.response.UserSignUpResponse;
import com.gxdxx.instagram.entity.RefreshToken;
import com.gxdxx.instagram.entity.User;
import com.gxdxx.instagram.exception.UnauthorizedAccessException;
import com.gxdxx.instagram.exception.NicknameAlreadyExistsException;
import com.gxdxx.instagram.exception.PasswordNotMatchException;
import com.gxdxx.instagram.exception.UserNotFoundException;
import com.gxdxx.instagram.jwt.JwtUtil;
import com.gxdxx.instagram.jwt.TokenDto;
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.RefreshTokenRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3Uploader s3Uploader;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponse saveUser(UserSignUpRequest request) {
        userRepository.findByNickname(request.nickname()).ifPresent(user -> {
            throw new NicknameAlreadyExistsException();
        });

        String profileImageUrl = s3Uploader.upload(request.profileImage(), "images");

        User saveUser = User.of(request.nickname(), passwordEncoder.encode(request.password()), profileImageUrl);
        return UserSignUpResponse.of(userRepository.save(saveUser));
    }

    public SuccessResponse deleteUser(Long id, String nickname) {
        User deleteUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!deleteUser.getNickname().equals(nickname)) {
            throw new UnauthorizedAccessException();
        }
        userRepository.delete(deleteUser);
        return SuccessResponse.of("회원탈퇴를 성공했습니다.");
    }

    public UserProfileResponse getProfile(String nickname) {
        User user = getUserFromNickname(nickname);
        Long followerCount = followRepository.countByFollowing(user);
        Long followingCount = followRepository.countByFollower(user);
        return UserProfileResponse.of(user.getNickname(), user.getProfileImageUrl(), followerCount, followingCount);
    }

    public UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest request, String nickname) {
        User user = getUserFromNickname(nickname);

        String profileImageUrl =  s3Uploader.upload(request.profileImage(), "images");

        user.updateProfile(request.nickname(), profileImageUrl);
        return UserProfileUpdateResponse.of(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

    private User getUserFromNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    public SuccessResponse login(UserLoginRequest request, HttpServletResponse response) {
        // 아이디 검사
        User user = userRepository.findByNickname(request.nickname()).orElseThrow(
                () -> new UserNotFoundException()
        );

        // 비밀번호 검사
        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }

        // 아이디 정보로 Token 생성
        TokenDto tokenDto = jwtUtil.createAllToken(request.nickname());

        // Refresh 토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByNickname(request.nickname());

        // 있다면 새 토큰 발급후 업데이트
        // 없다면 새로 만들고 DB 저장
        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.refreshToken()));
        } else {
            RefreshToken newToken = RefreshToken.of(tokenDto.refreshToken(), user.getNickname());
            refreshTokenRepository.save(newToken);
        }

        // response 헤더에 Access Token / Refresh Token 넣음
        setHeader(response, tokenDto);

        return new SuccessResponse("토큰이 발급되었습니다.");
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.accessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.refreshToken());
    }

}
