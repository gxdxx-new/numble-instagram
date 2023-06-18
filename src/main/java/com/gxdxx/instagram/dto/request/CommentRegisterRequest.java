package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentRegisterRequest(

        @Schema(description = "댓글 id")
        @JsonProperty("post_id")
        @Positive
        @NotNull
        Long postId,

        @Schema(description = "댓글 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
