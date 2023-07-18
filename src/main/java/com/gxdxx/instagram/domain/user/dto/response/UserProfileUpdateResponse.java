package com.gxdxx.instagram.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileUpdateResponse(

        @Schema(description = "회원 id")
        Long id,

        @Schema(description = "닉네임", example = "don")
        String nickname,

        @JsonProperty("profile_image_url")
        String profileImageUrl

) {

    public static UserProfileUpdateResponse of(Long id, String nickname, String profileImageUrl) {
        return new UserProfileUpdateResponse(id, nickname, profileImageUrl);
    }

}
