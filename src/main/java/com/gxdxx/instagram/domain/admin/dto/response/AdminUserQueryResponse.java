package com.gxdxx.instagram.domain.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AdminUserQueryResponse(

        @Schema(description = "회원 id")
        Long id,

        @Schema(description = "닉네임")
        String nickname,

        @Schema(description = "프로필 사진 url")
        @JsonProperty("profile_image_url")
        String profileImageUrl,

        @Schema(description = "탈퇴 여부")
        boolean deleted,

        @Schema(description = "가입일")
        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @Schema(description = "역할명")
        @JsonProperty("role_name")
        String roleName

) {
}
