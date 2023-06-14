package com.gxdxx.instagram.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostUpdateRequest(

        @Schema(description = "게시글 id")
        Long id,

        @Schema(description = "게시글 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content,

        @Schema(description = "사진")
        @NotNull
        MultipartFile image

) {
}
