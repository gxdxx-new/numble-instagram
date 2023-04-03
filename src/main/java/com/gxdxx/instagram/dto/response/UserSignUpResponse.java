package com.gxdxx.instagram.dto.response;

import com.gxdxx.instagram.entity.User;

public record UserSignUpResponse(Long id, String nickname, String profileImageUrl) {

    public static UserSignUpResponse of(User user) {
        return new UserSignUpResponse(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

}
