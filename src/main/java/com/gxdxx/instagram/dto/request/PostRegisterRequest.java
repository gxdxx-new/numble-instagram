package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostRegisterRequest(

        @Size(min = 2, max = 100)
        @NotBlank
        String content,

        MultipartFile image

) {
}
