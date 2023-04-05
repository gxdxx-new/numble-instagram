package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record UserSignUpRequest(@NotBlank String nickname, @NotBlank String password, MultipartFile profileImage) {
}
