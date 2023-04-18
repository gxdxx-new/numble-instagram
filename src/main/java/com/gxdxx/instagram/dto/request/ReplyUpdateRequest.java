package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReplyUpdateRequest(
        @Positive Long id,
        @NotBlank String content
) {
}
