package com.gxdxx.instagram.service;

import com.gxdxx.instagram.config.jwt.TokenProvider;
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
import com.gxdxx.instagram.repository.FollowRepository;
import com.gxdxx.instagram.repository.RefreshTokenRepository;
import com.gxdxx.instagram.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3Uploader s3Uploader;
    private final TokenProvider tokenProvider;
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

    @Transactional(readOnly = true)
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
