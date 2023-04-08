package com.gxdxx.instagram.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record UserProfileUpdateRequest(
        @JsonProperty("nickname") @NotBlank String nickname,
        @JsonProperty("profile_image") MultipartFile profileImage
) {
}
