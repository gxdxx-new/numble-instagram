package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MessageSendRequest(

        @JsonProperty("user_id")
        @Positive
        @NotNull
        Long userId,

        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
