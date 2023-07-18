package com.gxdxx.instagram.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record SuccessResponse(

        @Schema(description = "성공 메시지")
        @JsonProperty("success_message")
        String successMessage

) {

    public static SuccessResponse of(String successMessage) {
        return new SuccessResponse(successMessage);
    }

}
