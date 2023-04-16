package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PostUpdateRequest(
        Long id,
        @NotBlank String content,
        @NotNull MultipartFile image
) {
}
