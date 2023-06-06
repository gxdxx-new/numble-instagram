package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(

        @Size(min = 2, max = 20)
        @NotBlank
        String nickname,

        @Size(min = 4, max = 15)
        @NotBlank
        String password

) {
}
