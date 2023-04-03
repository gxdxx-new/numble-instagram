package com.gxdxx.instagram.dto.response;

public record ErrorResponse(String message) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }

}
