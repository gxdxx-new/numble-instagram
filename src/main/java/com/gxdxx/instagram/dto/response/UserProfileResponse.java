package com.gxdxx.instagram.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileResponse(
        
        @Schema(description = "닉네임", example = "don")
        String nickname,
        
        @Schema(description = "프로필 사진 url")
        @JsonProperty("profile_image_url") 
        String profileImageUrl,
        
        @Schema(description = "팔로워 수")
        Long follower,
        
        @Schema(description = "팔로잉 수")
        Long following
        
) {

    public static UserProfileResponse of(String nickname, String profileImageUrl, Long follower, Long following) {
        return new UserProfileResponse(nickname, profileImageUrl, follower, following);
    }

}
