package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ReplyRegisterRequest(

        @Positive
        @JsonProperty("comment_id")
        Long commentId,

        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
