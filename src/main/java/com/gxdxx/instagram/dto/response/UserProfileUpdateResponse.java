package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfileUpdateResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("nickname") String nickname,
        @JsonProperty("profile_image_url") String profileImageUrl
) {

    public static UserProfileUpdateResponse of(Long id, String nickname, String profileImageUrl) {
        return new UserProfileUpdateResponse(id, nickname, profileImageUrl);
    }

}
