package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfileResponse(
        @JsonProperty("nickname") String nickname,
        @JsonProperty("profile_image_url") String profileImageUrl,
        @JsonProperty("follower") Long follower,
        @JsonProperty("following") Long following
) {

    public static UserProfileResponse of(String nickname, String profileImageUrl, Long follower, Long following) {
        return new UserProfileResponse(nickname, profileImageUrl, follower, following);
    }

}
