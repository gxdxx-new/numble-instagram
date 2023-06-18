package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PostFeedRequest(

        @Schema(description = "회원 id")
        @JsonProperty("user_id")
        @Positive
        @NotNull
        Long userId,

        @Schema(description = "커서 (게시글 id)")
        Long cursor

) {
}
