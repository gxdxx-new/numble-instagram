package com.gxdxx.instagram.domain.reply.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ReplyRegisterRequest(

        @Schema(description = "답글 id")
        @JsonProperty("comment_id")
        @Positive
        @NotNull
        Long commentId,

        @Schema(description = "답글 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
