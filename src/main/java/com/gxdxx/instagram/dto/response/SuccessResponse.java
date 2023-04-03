package com.gxdxx.instagram.dto.response;

public record SuccessResponse(String message) {

    public static SuccessResponse of(String message) {
        return new SuccessResponse(message);
    }

}
