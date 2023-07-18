package com.gxdxx.instagram.global.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccessTokenCreateRequest(

        @Schema(description = "회원 id")
        @JsonProperty("user_id")
        @Positive
        @NotNull
        Long userId

) {
}
