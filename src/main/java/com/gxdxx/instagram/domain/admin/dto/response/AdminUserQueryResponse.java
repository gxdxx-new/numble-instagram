package com.gxdxx.instagram.domain.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AdminUserQueryResponse(

        Long id,

        String nickname,

        @JsonProperty("profile_image_url")
        String profileImageUrl,

        boolean deleted,

        @JsonProperty("created_at")
        LocalDateTime createdAt,

        @JsonProperty("role_name")
        String roleName

) {
}
