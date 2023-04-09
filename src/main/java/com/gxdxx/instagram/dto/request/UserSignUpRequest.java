package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public record UserSignUpRequest(
        @NotBlank String nickname,
        @NotBlank String password,
        @RequestPart(value = "profile_image") @NotNull MultipartFile profileImage
) {
}
