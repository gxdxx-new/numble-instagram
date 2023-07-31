package com.gxdxx.instagram.domain.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AdminUserQueryRequest(

        @Schema(description = "역할명")
        @JsonProperty("role_name")
        @NotBlank
        String roleName

) {
}
