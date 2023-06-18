package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gxdxx.instagram.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignUpResponse(

        @Schema(description = "회원 id")
        Long id,

        @Schema(description = "닉네임")
        String nickname,

        @Schema(description = "프로필 사진 url")
        @JsonProperty("profile_image_url")
        String profileImageUrl

) {

    public static UserSignUpResponse of(User user) {
        return new UserSignUpResponse(user.getId(), user.getNickname(), user.getProfileImageUrl());
    }

}
