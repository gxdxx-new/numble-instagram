package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentRegisterRequest(

        @Positive
        @JsonProperty("post_id")
        Long postId,

        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
