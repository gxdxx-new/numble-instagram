package com.gxdxx.instagram.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(

        @Schema(description = "댓글 id")
        @Positive
        @NotNull
        Long id,

        @Schema(description = "댓글 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
