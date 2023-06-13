package com.gxdxx.instagram.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserSignUpRequest(

        @Schema(description = "닉네임", example = "don")
        @Size(min = 2, max = 20)
        @NotBlank
        String nickname,

        @Schema(description = "비밀번호", example = "123123")
        @Size(min = 4, max = 15)
        @NotBlank
        String password,

        @Schema(description = "프로필 사진")
        @NotNull
        MultipartFile profileImage

) {
}
