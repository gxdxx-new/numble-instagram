package com.gxdxx.instagram.domain.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record AdminUserQueryRequest(

        @JsonProperty("role_name")
        @NotBlank
        String roleName

) {
}
