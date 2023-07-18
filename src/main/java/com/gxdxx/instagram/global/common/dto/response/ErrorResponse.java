package com.gxdxx.instagram.global.common.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(

        @Schema(description = "에러 메시지")
        @JsonProperty("error_message")
        String errorMessage

) {

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage);
    }

}
