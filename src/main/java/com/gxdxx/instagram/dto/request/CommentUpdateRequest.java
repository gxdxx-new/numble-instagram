package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequest(

        @Positive
        Long id,

        @Size(min = 2, max = 100)
        @NotBlank
        String content

) {
}
