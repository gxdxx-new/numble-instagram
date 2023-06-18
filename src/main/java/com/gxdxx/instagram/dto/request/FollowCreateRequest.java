package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FollowCreateRequest(

        @Schema(description = "요청한 회원이 팔로우할 회원의 id")
        @JsonProperty("user_id")
        @Positive
        @NotNull
        Long userId

) {
}
