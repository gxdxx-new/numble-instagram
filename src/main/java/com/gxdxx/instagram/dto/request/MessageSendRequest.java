package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MessageSendRequest(
        @JsonProperty("user_id") @Positive Long userId,
        @NotBlank String content
) {
}
