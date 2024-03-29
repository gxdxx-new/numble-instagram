package com.gxdxx.instagram.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostRegisterRequest(

        @Schema(description = "게시글 내용")
        @Size(min = 2, max = 100)
        @NotBlank
        String content,

        @Schema(description = "사진")
        @NotNull
        MultipartFile image

) {
}
