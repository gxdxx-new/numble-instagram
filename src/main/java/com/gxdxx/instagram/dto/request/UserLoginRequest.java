package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@NotBlank String nickname, @NotBlank String password) {
}
