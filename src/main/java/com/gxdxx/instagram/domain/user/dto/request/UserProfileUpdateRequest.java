package com.gxdxx.instagram.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserProfileUpdateRequest(

        @Schema(description = "닉네임", example = "don")
        @Size(min = 2, max = 20)
        @NotBlank
        String nickname,

        @Schema(description = "프로필 사진")
        @NotNull
        MultipartFile profileImage

) {
}
