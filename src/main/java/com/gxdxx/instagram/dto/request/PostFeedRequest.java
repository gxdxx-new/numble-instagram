package com.gxdxx.instagram.dto.request;

import jakarta.validation.constraints.Positive;

public record PostFeedRequest(

        @Positive
        Long userId,

        Long cursor

) {
}
