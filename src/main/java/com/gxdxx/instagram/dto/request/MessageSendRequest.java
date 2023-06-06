package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MessageSendRequest(

        @Positive
        @JsonProperty("user_id")
        Long userId,

        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
