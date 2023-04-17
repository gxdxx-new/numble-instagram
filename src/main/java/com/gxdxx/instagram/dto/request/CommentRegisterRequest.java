package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CommentRegisterRequest(
        @JsonProperty("post_id") @Positive Long postId,
        @NotBlank String content
) {
}
