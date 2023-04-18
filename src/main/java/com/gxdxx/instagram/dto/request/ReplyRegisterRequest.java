package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReplyRegisterRequest(
        @JsonProperty("comment_id") @Positive Long commentId,
        @NotBlank String content
) {
}
