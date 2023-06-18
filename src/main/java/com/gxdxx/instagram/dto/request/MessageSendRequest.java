package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MessageSendRequest(

        @Schema(description = "메시지를 받을 회원 id")
        @JsonProperty("user_id")
        @Positive
        @NotNull
        Long userId,

        @Schema(description = "메시지 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
